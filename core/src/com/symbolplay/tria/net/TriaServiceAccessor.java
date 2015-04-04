package com.symbolplay.tria.net;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.symbolplay.gamelibrary.game.GameContainerUpdateable;
import com.symbolplay.gamelibrary.net.HttpRequestTask;
import com.symbolplay.gamelibrary.net.HttpRequestTaskFinishedListener;
import com.symbolplay.gamelibrary.net.QueryParameter;
import com.symbolplay.gamelibrary.persistence.JsonReaderUtils;
import com.symbolplay.gamelibrary.util.EncryptionUtil;
import com.symbolplay.gamelibrary.util.HexUtils;
import com.symbolplay.gamelibrary.util.Logger;
import com.symbolplay.tria.persistence.GameEncryptionKeys;
import com.symbolplay.tria.persistence.userdata.HighScoreData;

public final class TriaServiceAccessor implements GameContainerUpdateable {
    
    private static final String TRIA_SERVICE_BASE_ADDRESS = "http://symbolplay.com/tria_service";
    
    private static final int DEFAULT_SCORES_BUFFER_SIZE = 10000;
    private static final int DEFAULT_SCORE_UPDATE_RESULT_BUFFER_SIZE = 200;
    
    private static final String CHARSET = "UTF-8";
    
    private Array<HighScoreData> scoresData;
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
                        Array<HighScoreData> scoresData = getScoresData(scoresDataString);
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
    
    public void publishGlobalScore(String name, int score, int appVersion) {
        
        String queryString = "method=add_score&name=" + name + "&score=" + score + "&app_version=" + appVersion;
        
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
        //queryParameters.add(new QueryParameter("encrypted", encryptedQueryStringValue));
        
        // TODO: remove this and use encryption
        queryParameters.add(new QueryParameter("method", "add_score"));
        queryParameters.add(new QueryParameter("name", name));
        queryParameters.add(new QueryParameter("score", String.valueOf(score)));
        queryParameters.add(new QueryParameter("app_version", String.valueOf(appVersion)));
        
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
    private static Array<HighScoreData> getScoresData(String jsonText) {
        JsonValue jsonValue = new JsonReader().parse(jsonText);
        Array<Array<Object>> rawScoresData = (Array<Array<Object>>) JsonReaderUtils.jsonValueToObject(jsonValue);
        
        Array<HighScoreData> scoresData = new Array<HighScoreData>(false, rawScoresData.size);
        for (Array<Object> rawScoreData : rawScoresData) {
            String name = rawScoreData.get(0).toString();
            int score = Integer.parseInt(rawScoreData.get(1).toString());
            long time = 0L; // ignore time, scores from server are already sorted
            HighScoreData scoreData = new HighScoreData(name, score, time); 
            scoresData.add(scoreData);
        }
        
        return scoresData;
    }
    
    private void setScoresData(Array<HighScoreData> scoresData) {
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
            
            Array<HighScoreData> scoresDataCopy;
            if (scoresData != null) {
                scoresDataCopy = new Array<HighScoreData>(scoresData.size);
                scoresDataCopy.addAll(scoresData);
            } else {
                scoresDataCopy = null;
            }
            notifyGlobalScoresReceived(scoresDataCopy);
            
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
    
    private void notifyGlobalScoresReceived(Array<HighScoreData> scoresData) {
        for (TriaServiceScoreListener scoreListener : scoreListeners) {
            scoreListener.scoresReceived(scoresData);
        }
    }
    
    private void notifyError(String message) {
        for (TriaServiceScoreListener scoreListener : scoreListeners) {
            scoreListener.error(message);
        }
    }
}
