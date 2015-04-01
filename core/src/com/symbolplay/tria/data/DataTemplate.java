package com.symbolplay.tria.data;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Keys;

public final class DataTemplate {
    
    public final String name;
    private final ObjectMap<String, String> properties;
    
    public DataTemplate(String name, ObjectMap<String, String> properties) {
        this.name = name;
        this.properties = properties;
    }
    
    public String getName() {
        return name;
    }
    
    public String getProperty(String name) {
        return properties.get(name);
    }
    
    public boolean hasProperty(String name) {
        return properties.containsKey(name);
    }
    
    public Keys<String> getPropertyNames() {
        return properties.keys();
    }
}
