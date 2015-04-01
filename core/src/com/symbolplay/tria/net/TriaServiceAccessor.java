package com.symbolplay.tria.net;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.symbolplay.gamelibrary.game.GameContainerUpdateable;
import com.symbolplay.gamelibrary.net.HttpRequestTask;
import com.symbolplay.gamelibrary.net.HttpRequestTaskFinishedListener;
import com.symbolplay.gamelibrary.net.QueryParameter;
import com.symbolplay.gamelibrary.persistence.JsonReaderUtils;
import com.symbolplay.gamelibrary.util.EncryptionUtil;
import com.symbolplay.gamelibrary.util.HexUtils;
import com.symbolplay.gamelibrary.util.Logger;
import com.symbolplay.tria.persistence.GameEncryptionKeys;

public final class TriaServiceAccessor implements GameContainerUpdateable {
    
    private static final String TRIA_SERVICE_BASE_ADDRESS = "http://symbolplay.com/tria_service";
    
    private static final int DEFAULT_SCORES_BUFFER_SIZE = 10000;
    private static final int DEFAULT_SCORE_UPDATE_RESULT_BUFFER_SIZE = 200;
    
    private static final String CHARSET = "UTF-8";
    
    private Array<TriaServiceScoreData> scoresData;
    private String errorMessage;
    
    private boolean isScoresReceived;
    
    private final Object scoreLock = new Object();
    
    private Array<TriaServiceScoreListener> scoreListeners; 
    
    public TriaServiceAccessor() {
        scoreListeners = new Array<TriaServiceScoreListener>(true, 10);
        
        clearData();
    }
    
    @Override
    public void update(float delta) {
        if (isScoresReceived) {
            handleScoresReceived();
        }
        
        if (errorMessage != null) {
            handleErrorMessage();
        }
    }
    
    public void clearData() {
        synchronized (scoreLock) {
            scoresData = null;
            errorMessage = null;
            isScoresReceived = false;
        }
    }
    
    public void requestGlobalScores() {
        
        Array<QueryParameter> queryParameters = new Array<QueryParameter>(true, 1);
        queryParameters.add(new QueryParameter("method", "get_global_scores"));
        
        final HttpRequestTask httpRequestTask = new HttpRequestTask(
                "GlobalScoresTask",
                TRIA_SERVICE_BASE_ADDRESS,
                queryParameters,
                false,
                DEFAULT_SCORES_BUFFER_SIZE,
                false);
        
        httpRequestTask.setFinishedListener(new HttpRequestTaskFinishedListener() {
            
            @Override
            public void httpRequestTaskFinished() {
                String scoresDataString = (String) httpRequestTask.getData();
                if (!StringUtils.isEmpty(scoresDataString)) {
                    try {
                        Array<TriaServiceScoreData> scoresData = getScoresData(scoresDataString);
                        setScoresData(scoresData);
                    } catch (Exception e) {
                        setErrorMessage("Failed to get global scores from the server.");
                        Logger.error("Failed to get valid global scores data from the server.");
                    }
                } else {
                    String errorMessage = "Failed to get global scores from the server.";
                    setErrorMessage(errorMessage);
                    Logger.error(errorMessage);
                }
            }
        });
        httpRequestTask.start();
    }
    
    public void requestFacebookScores(Array<String> userIds) {
        
        Array<QueryParameter> queryParameters = new Array<QueryParameter>(true, userIds.size + 1);
        queryParameters.add(new QueryParameter("method", "get_facebook_scores"));
        for (String userId : userIds) {
            queryParameters.add(new QueryParameter("user_ids[]", String.valueOf(userId)));
        }
        
        final HttpRequestTask httpRequestTask = new HttpRequestTask(
                "FacebookScoresTask",
                TRIA_SERVICE_BASE_ADDRESS,
                queryParameters,
                false,
                DEFAULT_SCORES_BUFFER_SIZE,
                false);
        
        httpRequestTask.setFinishedListener(new HttpRequestTaskFinishedListener() {
            
            @Override
            public void httpRequestTaskFinished() {
                String scoresDataString = (String) httpRequestTask.getData();
                if (!StringUtils.isEmpty(scoresDataString)) {
                    try {
                        Array<TriaServiceScoreData> scoresData = getScoresData(scoresDataString);
                        setScoresData(scoresData);
                    } catch (Exception e) {
                        setErrorMessage("Failed to get Facebook scores from the server.");
                        Logger.error("Failed to get valid Facebook scores data from the server.");
                    }
                } else {
                    String errorMessage = "Failed to get Facebook scores from the server.";
                    setErrorMessage(errorMessage);
                    Logger.error(errorMessage);
                }
            }
        });
        httpRequestTask.start();
    }
    
    public void publishGlobalAndFacebookScore(String userId, int score) {
        
        String queryString = "method=update_score&user_id=" + userId + "&score=" + score;
        
        String encryptedQueryStringValue = null;
        try {
            byte[] queryStringBytes = queryString.getBytes(CHARSET);
            byte[] encryptedQueryStringBytes = EncryptionUtil.encryptData(queryStringBytes, GameEncryptionKeys.getTriaServiceEncryptionInputData());
            encryptedQueryStringValue = HexUtils.bytesToHex(encryptedQueryStringBytes);
        } catch (Exception e) {
            Logger.error("Failed to encrypt update score query string");
            return;
        }
        
        Array<QueryParameter> queryParameters = new Array<QueryParameter>(true, 1);
        queryParameters.add(new QueryParameter("encrypted", encryptedQueryStringValue));
        
        final HttpRequestTask httpRequestTask = new HttpRequestTask(
                "UpdateCurrentUserScoreTask",
                TRIA_SERVICE_BASE_ADDRESS,
                queryParameters,
                false,
                DEFAULT_SCORE_UPDATE_RESULT_BUFFER_SIZE,
                true);
        
        httpRequestTask.setFinishedListener(new HttpRequestTaskFinishedListener() {
            
            @Override
            public void httpRequestTaskFinished() {
                String updateScoresResultString = (String) httpRequestTask.getData();
                if (!StringUtils.isEmpty(updateScoresResultString)) {
                    Logger.info("Update score executed with result: "+ updateScoresResultString);
                } else {
                    Logger.error("Failed to publish score to the server.");
                    // currently we don't set errorMessage in this case
                }
            }
        });
        httpRequestTask.start();
    }
    
    public void publishFacebookScore(String userId, int score) {
        
        String queryString = "method=update_facebook_score&user_id=" + userId + "&score=" + score;
        
        String encryptedQueryStringValue = null;
        try {
            byte[] queryStringBytes = queryString.getBytes(CHARSET);
            byte[] encryptedQueryStringBytes = EncryptionUtil.encryptData(queryStringBytes, GameEncryptionKeys.getTriaServiceEncryptionInputData());
            encryptedQueryStringValue = HexUtils.bytesToHex(encryptedQueryStringBytes);
        } catch (Exception e) {
            Logger.error("Failed to encrypt update score query string");
            return;
        }
        
        Array<QueryParameter> queryParameters = new Array<QueryParameter>(true, 1);
        queryParameters.add(new QueryParameter("encrypted", encryptedQueryStringValue));
        
        final HttpRequestTask httpRequestTask = new HttpRequestTask(
                "UpdateCurrentUserScoreTask",
                TRIA_SERVICE_BASE_ADDRESS,
                queryParameters,
                false,
                DEFAULT_SCORE_UPDATE_RESULT_BUFFER_SIZE,
                true);
        
        httpRequestTask.setFinishedListener(new HttpRequestTaskFinishedListener() {
            
            @Override
            public void httpRequestTaskFinished() {
                String updateScoresResultString = (String) httpRequestTask.getData();
                if (!StringUtils.isEmpty(updateScoresResultString)) {
                    Logger.info("Update score executed with result: "+ updateScoresResultString);
                } else {
                    Logger.error("Failed to publish score to the server.");
                    // currently we don't set errorMessage in this case
                }
            }
        });
        httpRequestTask.start();
    }
    
    @SuppressWarnings("unchecked")
    private static Array<TriaServiceScoreData> getScoresData(String jsonText) {
        JsonValue jsonValue = new JsonReader().parse(jsonText);
        Array<Array<Long>> rawScoresData = (Array<Array<Long>>) JsonReaderUtils.jsonValueToObject(jsonValue);
        
        Array<TriaServiceScoreData> scoresData = new Array<TriaServiceScoreData>(false, rawScoresData.size);
        for (Array<Long> rawScoreData : rawScoresData) {
            TriaServiceScoreData scoreData = new TriaServiceScoreData(rawScoreData.get(0).toString(), rawScoreData.get(1).intValue());
            scoresData.add(scoreData);
        }
        
        return scoresData;
    }
    
    private void setScoresData(Array<TriaServiceScoreData> scoresData) {
        synchronized (scoreLock) {
            this.scoresData = scoresData;
            isScoresReceived = true;
        }
    }
    
    private void setErrorMessage(String message) {
        synchronized (scoreLock) {
            errorMessage = message;
        }
    }
    
    private void handleScoresReceived() {
        synchronized (scoreLock) {
            if (!isScoresReceived) {
                return;
            }
            
            ObjectMap<String, TriaServiceScoreData> scoreDataMap;
            if (scoresData != null) {
                scoreDataMap = new ObjectMap<String, TriaServiceScoreData>(scoresData.size);
                for (TriaServiceScoreData scoreData : scoresData) {
                    scoreDataMap.put(scoreData.getUserId(), scoreData);
                }
            } else {
                scoreDataMap = null;
            }
            notifyFriendScoresReceived(scoreDataMap);
            
            isScoresReceived = false;
        }
    }
    
    private void handleErrorMessage() {
        synchronized (scoreLock) {
            if (errorMessage == null) {
                return;
            }
            
            notifyError(errorMessage);
            
            errorMessage = null;
        }
    }
    
    public void addScoreListener(TriaServiceScoreListener scoreListener) {
        scoreListeners.add(scoreListener);
    }
    
    public void removeScoreListener(TriaServiceScoreListener scoreListener) {
        scoreListeners.removeValue(scoreListener, true);
    }
    
    public void clearScoreListeners() {
        scoreListeners.clear();
    }
    
    private void notifyFriendScoresReceived(ObjectMap<String, TriaServiceScoreData> scoreDataMap) {
        for (TriaServiceScoreListener scoreListener : scoreListeners) {
            scoreListener.scoresReceived(scoreDataMap);
        }
    }
    
    private void notifyError(String message) {
        for (TriaServiceScoreListener scoreListener : scoreListeners) {
            scoreListener.error(message);
        }
    }
}
