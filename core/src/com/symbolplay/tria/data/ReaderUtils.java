package com.symbolplay.tria.data;

import java.io.IOException;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.ObjectMap.Keys;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.symbolplay.gamelibrary.util.ExceptionThrower;
import com.symbolplay.gamelibrary.util.Logger;

final class ReaderUtils {
    
    public static Element getRootNode(FileHandle fileHandle) {
        XmlReader reader = new XmlReader();
        
        Element rootNode = null;
        try {
            rootNode = reader.parse(fileHandle);
        } catch (IOException e) {
            Logger.error(e.getMessage());
            return null;
        }
        
        return rootNode;
    }
    
    public static void addDataTemplates(ObjectMap<String, DataTemplate> existingDataTemplates, Element dataTemplatesNode) {
        int numDataTemplates = dataTemplatesNode.getChildCount();
        for (int i = 0; i < numDataTemplates; i++) {
            Element dataTemplateNode = dataTemplatesNode.getChild(i);
            addDataTemplate(existingDataTemplates, dataTemplateNode);
        }
    }
    
    private static void addDataTemplate(ObjectMap<String, DataTemplate> existingDataTemplates, Element dataTemplateNode) {
        String name = dataTemplateNode.getAttribute("name");
        ObjectMap<String, String> properties = getProperties(dataTemplateNode.getChildByName("properties"), existingDataTemplates);
        DataTemplate dataTemplate = new DataTemplate(name, properties);
        existingDataTemplates.put(dataTemplate.getName(), dataTemplate);
    }
    
    public static ObjectMap<String, String> getProperties(Element propertiesNode) {
        return getProperties(propertiesNode, null);
    }
    
    public static ObjectMap<String, String> getProperties(Element propertiesNode, ObjectMap<String, DataTemplate> existingDataTemplates) {
        
        if (propertiesNode == null) {
            return null;
        }
        
        int numProperties = propertiesNode.getChildCount();
        ObjectMap<String, String> properties = new ObjectMap<String, String>(numProperties);
        for (int i = 0; i < numProperties; i++) {
            Element propertyNode = propertiesNode.getChild(i);
            String name = propertyNode.getAttribute("name");
            String value = propertyNode.getAttribute("value");
            properties.put(name, value);
        }
        
        if (existingDataTemplates != null) {
            String dataTemplateNamesStr = propertiesNode.get("datatemplates", null);
            
            if (dataTemplateNamesStr != null) {
                String[] dataTemplateNames = dataTemplateNamesStr.split(",");
                
                for (String dataTemplateName : dataTemplateNames) {
                    DataTemplate dataTemplate = existingDataTemplates.get(dataTemplateName);
                    
                    if (dataTemplate != null) {
                        Keys<String> dataTemplatePropertyNames = dataTemplate.getPropertyNames();
                        for (String dataTemplatePropertyName : dataTemplatePropertyNames) {
                            if (!properties.containsKey(dataTemplatePropertyName)) {
                                String dataTemplatePropertyValue = dataTemplate.getProperty(dataTemplatePropertyName);
                                properties.put(dataTemplatePropertyName, dataTemplatePropertyValue);
                            }
                        }
                    }
                }
            }
        }
        
        return properties;
    }
    
    public static String getStringAttribute(Element node, String attributeName, DataTemplate[] dataTemplates) {
        if (hasAttribute(node, attributeName)) {
            return node.getAttribute(attributeName);
        } else {
            String attribute = getProperty(dataTemplates, attributeName);
            if (attribute != null) {
                return attribute;
            } else {
                ExceptionThrower.throwException("Element '%s' doesn't have attribute '%s'.", node.getName(), attributeName);
                return null;
            }
        }
    }
    
    public static String getStringAttribute(Element node, String attributeName, DataTemplate[] dataTemplates, String defaultValue) {
        if (hasAttribute(node, attributeName)) {
            return node.getAttribute(attributeName);
        } else {
            String attribute = getProperty(dataTemplates, attributeName);
            if (attribute != null) {
                return attribute;
            } else {
                return defaultValue;
            }
        }
    }
    
    public static int getIntAttribute(Element node, String attributeName, DataTemplate[] dataTemplates) {
        if (hasAttribute(node, attributeName)) {
            return node.getIntAttribute(attributeName);
        } else {
                String attribute = getProperty(dataTemplates, attributeName);
            if (attribute != null) {
                return Integer.parseInt(attribute);
            } else {
                ExceptionThrower.throwException("Element '%s' doesn't have attribute '%s'.", node.getName(), attributeName);
                return 0;
            }
        }
    }
    
    public static int getIntAttribute(Element node, String attributeName, DataTemplate[] dataTemplates, int defaultValue) {
        if (hasAttribute(node, attributeName)) {
            return node.getIntAttribute(attributeName);
        } else {
            String attribute = getProperty(dataTemplates, attributeName);
            if (attribute != null) {
                return Integer.parseInt(attribute);
            } else {
                return defaultValue;
            }
        }
    }
    
    public static float getFloatAttribute(Element node, String attributeName, DataTemplate[] dataTemplates) {
        if (hasAttribute(node, attributeName)) {
            return node.getFloatAttribute(attributeName);
        } else {
            String attribute = getProperty(dataTemplates, attributeName);
            if (attribute != null) {
                return Float.parseFloat(attribute);
            } else {
                ExceptionThrower.throwException("Element '%s' doesn't have attribute '%s'.", node.getName(), attributeName);
                return 0.0f;
            }
        }
    }
    
    public static float getFloatAttribute(Element node, String attributeName, DataTemplate[] dataTemplates, float defaultValue) {
        if (hasAttribute(node, attributeName)) {
            return node.getFloatAttribute(attributeName);
        } else {
            String attribute = getProperty(dataTemplates, attributeName);
            if (attribute != null) {
                return Float.parseFloat(attribute);
            } else {
                return defaultValue;
            }
        }
    }
    
    public static boolean getBooleanAttribute(Element node, String attributeName, DataTemplate[] dataTemplates) {
        if (hasAttribute(node, attributeName)) {
            return node.getBooleanAttribute(attributeName);
        } else {
            String attribute = getProperty(dataTemplates, attributeName);
            if (attribute != null) {
                return Boolean.parseBoolean(attribute);
            } else {
                ExceptionThrower.throwException("Element '%s' doesn't have attribute '%s'.", node.getName(), attributeName);
                return false;
            }
        }
    }
    
    public static boolean getBooleanAttribute(Element node, String attributeName, DataTemplate[] dataTemplates, boolean defaultValue) {
        if (hasAttribute(node, attributeName)) {
            return node.getBooleanAttribute(attributeName);
        } else {
            String attribute = getProperty(dataTemplates, attributeName);
            if (attribute != null) {
                return Boolean.parseBoolean(attribute);
            } else {
                return defaultValue;
            }
        }
    }
    
    private static boolean hasAttribute(Element node, String attributeName) {
        return node.getAttributes().containsKey(attributeName);
    }
    
    private static String getProperty(DataTemplate[] dataTemplates, String attributeName) {
        if (dataTemplates == null) {
            return null;
        }
        
        for (DataTemplate dataTemplate : dataTemplates) {
            if (dataTemplate != null && dataTemplate.hasProperty(attributeName)) {
                return dataTemplate.getProperty(attributeName);
            }
        }
        
        return null;
    }
}
