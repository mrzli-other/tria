package com.symbolplay.tria.net;

import java.util.Comparator;

import org.apache.commons.lang3.StringUtils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.symbolplay.gamelibrary.game.GameContainerUpdateable;
import com.symbolplay.gamelibrary.net.HttpRequestTask;
import com.symbolplay.gamelibrary.net.HttpRequestTaskFinishedListener;
import com.symbolplay.gamelibrary.net.QueryParameter;
import com.symbolplay.gamelibrary.util.GameUtils;
import com.symbolplay.gamelibrary.util.Logger;

public final class NewsAccessor implements GameContainerUpdateable {
    
    private static final String NEWS_SERVICE_BASE_ADDRESS = "http://symbolplay.com/news_service";
    
    private static final int NEWS_SHOW_STEP = 5;
    private static final int NEWS_SHOW_MAX = 20;
    
    private static final int DEFAULT_LAST_NEWS_INDEX_BUFFER_SIZE = 10;
    private static final int DEFAULT_NEWS_BUFFER_SIZE = 10000;
    private static final int DEFAULT_IMAGE_BUFFER_SIZE = 65536;
    
    private static final Comparator<NewsData> NEWS_COMPARATOR;
    
    private static final int LISTENER_CAPACITY = 10;
    
    private int lastNewsIndex;
    private final Array<NewsData> newsList;
    private final ObjectMap<String, byte[]> newsImageDataMap;
    private String errorMessage;
    
    private boolean isLastNewsIndexBeingRequested;
    private boolean isNewsBeingRequested;
    private final ObjectSet<String> imagesBeingRequested;
    
    private boolean isLastNewsIndexReceived;
    private boolean isNewsDataReceived;
    
    private int lowestNewsIndexToRequest;
    
    private final Object newsLock = new Object();
    
    private Array<NewsListener> newsListeners; 
    
    static {
        NEWS_COMPARATOR = new Comparator<NewsData>() {
            
            @Override
            public int compare(NewsData n1, NewsData n2) {
                return -GameUtils.compareInteger(n1.getIndex(), n2.getIndex());
            }
        };
    }
    
    public NewsAccessor() {
        newsList = new Array<NewsData>(true, NEWS_SHOW_MAX);
        imagesBeingRequested = new ObjectSet<String>(NEWS_SHOW_MAX);
        newsImageDataMap = new ObjectMap<String, byte[]>(NEWS_SHOW_MAX);
        
        newsListeners = new Array<NewsListener>(true, LISTENER_CAPACITY);
        
        clearData();
    }
    
    @Override
    public void update(float delta) {
        if (isLastNewsIndexReceived) {
            handleLastNewsIndexReceived();
        }
        
        if (isNewsDataReceived) {
            handleNewsDataReceived();
        }
        
        if (errorMessage != null) {
            handleErrorMessage();
        }
    }

    public void clearData() {
        synchronized (newsLock) {
            lastNewsIndex = -1;
            newsList.clear();
            newsImageDataMap.clear();
            errorMessage = null;
            
            isLastNewsIndexBeingRequested = false;
            isNewsBeingRequested = false;
            imagesBeingRequested.clear();
            
            lowestNewsIndexToRequest = -1;
            
            isLastNewsIndexReceived = false;
            isNewsDataReceived = false;
        }
    }
    
    public void requestLastNewsIndex() {
        if (!tryEnterLastNewsIndexRequest()) {
            return;
        }
        
        if (tryUseCachedLastNewsIndex()) {
            exitLastNewsIndexRequest();
            return;
        }
        
        Array<QueryParameter> queryParameters = new Array<QueryParameter>(true, 1);
        queryParameters.add(new QueryParameter("method", "get_last_news_index"));
        
        final HttpRequestTask httpRequestTask = new HttpRequestTask(
                "LastNewsIndexTask",
                NEWS_SERVICE_BASE_ADDRESS,
                queryParameters,
                false,
                DEFAULT_LAST_NEWS_INDEX_BUFFER_SIZE,
                false);
        
        httpRequestTask.setFinishedListener(new HttpRequestTaskFinishedListener() {
            
            @Override
            public void httpRequestTaskFinished() {
                String lastNewsIndexString = (String) httpRequestTask.getData();
                if (!StringUtils.isEmpty(lastNewsIndexString)) {
                    try {
                        int lastNewsIndex = Integer.parseInt(lastNewsIndexString);
                        setLastNewsIndex(lastNewsIndex);
                    } catch (Exception e) {
                        setErrorMessage("Failed to get news from the server.");
                        Logger.error("Failed to receive valid last news index from the server.");
                    }
                } else {
                    setErrorMessage("Failed to get news from the server.");
                    Logger.error("Failed to get last news index from server.");
                }
                
                exitLastNewsIndexRequest();
            }
        });
        httpRequestTask.start();
    }
    
    @SuppressWarnings("unchecked")
    public void requestInitialOrNextNewsGroup(boolean isInitial) {
        if (!tryEnterNewsRequest()) {
            return;
        }
        
        int index1 = 0;
        int index2 = 0;
        synchronized (newsLock) {
            if (lastNewsIndex <= 0) {
                exitNewsRequest();
                return;
            }
            if (isInitial && newsList.size > 0) {
                // this is where we also check if any images are missing so they can be requsted again
                requestNewsImagesAgainIfNecessary();
                isNewsDataReceived = true;
                exitNewsRequest();
                return;
            }
            int minReceivedNewsIndex = newsList.size > 0 ? newsList.peek().getIndex() : lastNewsIndex + 1;
            index1 = minReceivedNewsIndex - 1;
            index2 = Math.max(index1 - NEWS_SHOW_STEP + 1, lowestNewsIndexToRequest);
            if (index1 < index2) {
                isNewsDataReceived = true;
                exitNewsRequest();
                return;
            }
        }
        
        Array<QueryParameter> queryParameters = new Array<QueryParameter>(true, 3);
        queryParameters.add(new QueryParameter("method", "get_news"));
        queryParameters.add(new QueryParameter("index1", String.valueOf(index1)));
        queryParameters.add(new QueryParameter("index2", String.valueOf(index2)));
        
        final HttpRequestTask httpRequestTask = new HttpRequestTask(
                "GetNewsTask",
                NEWS_SERVICE_BASE_ADDRESS,
                queryParameters,
                false,
                DEFAULT_NEWS_BUFFER_SIZE,
                false);
        
        httpRequestTask.setFinishedListener(new HttpRequestTaskFinishedListener() {
            
            @Override
            public void httpRequestTaskFinished() {
                String newsDataString = (String) httpRequestTask.getData();
                if (!StringUtils.isEmpty(newsDataString)) {
                    try {
                        Array<NewsData> newsList = (new Json()).fromJson(Array.class, NewsData.class, newsDataString);
                        for (NewsData newsData : newsList) {
                            String imageName = newsData.getImage();
                            if (!StringUtils.isEmpty(imageName)) {
                                requestNewsImageData(imageName);
                            }
                        }
                        setNewsItems(newsList);
                    } catch (Exception e) {
                        setErrorMessage("Failed to get news from the server.");
                        Logger.error("Failed to receive valid news data from the server.");
                    }
                } else {
                    String errorMessage = "Failed to get news from the server.";
                    setErrorMessage(errorMessage);
                    Logger.error(errorMessage);
                }
                
                exitNewsRequest();
            }
        });
        httpRequestTask.start();
    }
    
    private void requestNewsImageData(final String imageName) {
        if (!tryEnterNewsImageRequest(imageName)) {
            return;
        }
        
        String imageUrl = NEWS_SERVICE_BASE_ADDRESS + "/images/" + imageName;
        
        final HttpRequestTask httpRequestTask = new HttpRequestTask(
                "GetNewsImageTask",
                imageUrl,
                null,
                true,
                DEFAULT_IMAGE_BUFFER_SIZE,
                false);
        
        httpRequestTask.setFinishedListener(new HttpRequestTaskFinishedListener() {
            
            @Override
            public void httpRequestTaskFinished() {
                byte[] imageData = (byte[]) httpRequestTask.getData();
                if (imageData != null) {
                    setNewsImage(imageName, imageData);
                } else {
                    setErrorMessage("Failed to get news image from server.");
                    Logger.error("Failed to get news image '%s' from server.", imageName);
                }
                
                exitNewsImageRequest(imageName);
            }
        });
        httpRequestTask.start();
    }
    
    public boolean hasMoreNewsToRequest() {
        synchronized (newsLock) {
            int minReceivedNewsIndex = newsList.size > 0 ? newsList.peek().getIndex() : lastNewsIndex + 1;
            return minReceivedNewsIndex > lowestNewsIndexToRequest;
        }
    }
    
    private boolean tryEnterLastNewsIndexRequest() {
        synchronized (newsLock) {
            if (!isLastNewsIndexBeingRequested) {
                isLastNewsIndexBeingRequested = true;
                return true;
            } else {
                return false;
            }
        }
    }
    
    private void exitLastNewsIndexRequest() {
        synchronized (newsLock) {
            isLastNewsIndexBeingRequested = false;
        }
    }
    
    private boolean tryEnterNewsRequest() {
        synchronized (newsLock) {
            if (!isNewsBeingRequested) {
                isNewsBeingRequested = true;
                return true;
            } else {
                return false;
            }
        }
    }
    
    private void exitNewsRequest() {
        synchronized (newsLock) {
            isNewsBeingRequested = false;
        }
    }
    
    private boolean tryEnterNewsImageRequest(String imageName) {
        synchronized (newsLock) {
            if (!newsImageDataMap.containsKey(imageName) && !imagesBeingRequested.contains(imageName)) {
                imagesBeingRequested.add(imageName);
                return true;
            } else {
                return false;
            }
        }
    }
    
    private void exitNewsImageRequest(String imageName) {
        synchronized (newsLock) {
            imagesBeingRequested.remove(imageName);
        }
    }
    
    private boolean tryUseCachedLastNewsIndex() {
        if (lastNewsIndex >= 0) {
            synchronized (newsLock) {
                if (lastNewsIndex >= 0) {
                    isLastNewsIndexReceived = true;
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void requestNewsImagesAgainIfNecessary() {
        synchronized (newsLock) {
            for (NewsData newsData : newsList) {
                String imageName = newsData.getImage();
                if (!StringUtils.isEmpty(imageName) && !newsImageDataMap.containsKey(imageName)) {
                    requestNewsImageData(imageName);
                }
            }
        }
    }
    
    private void setLastNewsIndex(int lastNewsIndex) {
        synchronized (newsLock) {
            this.lastNewsIndex = lastNewsIndex;
            lowestNewsIndexToRequest = Math.max(lastNewsIndex - NEWS_SHOW_MAX + 1, 1);
            isLastNewsIndexReceived = true;
        }
    }
    
    private void setNewsItems(Array<NewsData> receivedNewsList) {
        synchronized (newsLock) {
            for (NewsData newsData : receivedNewsList) {
                newsList.add(newsData);
            }
            newsList.sort(NEWS_COMPARATOR);
            isNewsDataReceived = true;
        }
    }
    
    private void setNewsImage(String imageName, byte[] imageData) {
        synchronized (newsLock) {
            newsImageDataMap.put(imageName, imageData);
            isNewsDataReceived = true;
        }
    }
    
    private void setErrorMessage(String message) {
        synchronized (newsLock) {
            errorMessage = message;
        }
    }
    
    private void handleLastNewsIndexReceived() {
        synchronized (newsLock) {
            if (!isLastNewsIndexReceived) {
                return;
            }
            
            notifyLastNewsIndexReceived(lastNewsIndex);
            
            isLastNewsIndexReceived = false;
        }
    }
    
    private void handleNewsDataReceived() {
        synchronized (newsLock) {
            if (!isNewsDataReceived) {
                return;
            }
            
            Array<NewsAndImageData> newsAndImageDataList = new Array<NewsAndImageData>(true, newsList.size);
            for (NewsData newsData : newsList) {
                byte[] imageData = newsImageDataMap.get(newsData.getImage(), null);
                NewsAndImageData newsAndImageData = new NewsAndImageData(newsData, imageData);
                newsAndImageDataList.add(newsAndImageData);
            }
            
            notifyNewsDataReceived(newsAndImageDataList);
            
            isNewsDataReceived = false;
        }
    }
    
    private void handleErrorMessage() {
        synchronized (newsLock) {
            if (errorMessage == null) {
                return;
            }
            
            notifyError(errorMessage);
            
            errorMessage = null;
        }
    }
    
    public void addNewsListener(NewsListener newsListener) {
        newsListeners.add(newsListener);
    }
    
    public void removeNewsListener(NewsListener newsListener) {
        newsListeners.removeValue(newsListener, true);
    }
    
    public void clearNewsListeners() {
        newsListeners.clear();
    }
    
    private void notifyLastNewsIndexReceived(int lastNewsIndex) {
        for (NewsListener newsListener : newsListeners) {
            newsListener.lastNewsIndexReceived(lastNewsIndex);
        }
    } 
    
    private void notifyNewsDataReceived(Array<NewsAndImageData> newsAndImageDataList) {
        for (NewsListener newsListener : newsListeners) {
            newsListener.newsDataReceived(newsAndImageDataList);
        }
    }
    
    private void notifyError(String message) {
        for (NewsListener newsListener : newsListeners) {
            newsListener.error(message);
        }
    }
}
