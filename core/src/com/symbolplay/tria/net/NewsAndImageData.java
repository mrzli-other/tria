package com.symbolplay.tria.net;

public final class NewsAndImageData {
    
    private final NewsData newsData;
    private final byte[] imageData;
    
    public NewsAndImageData(NewsData newsData, byte[] imageData) {
        this.newsData = newsData;
        this.imageData = imageData;
    }
    
    public NewsData getNewsData() {
        return newsData;
    }
    
    public byte[] getImageData() {
        return imageData;
    }
}
