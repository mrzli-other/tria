package com.symbolplay.tria.net;

public final class FacebookProfileData {
    
    private final String id;
    private final String name;
    
    public FacebookProfileData(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
}
