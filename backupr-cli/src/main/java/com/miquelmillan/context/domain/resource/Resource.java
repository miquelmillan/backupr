package com.miquelmillan.context.domain.resource;

import java.util.Map;

public class Resource {

    private String name;
    private String location;
    private Map<String, Object> properties;

    public Resource(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Resource(String name, String location, Map<String, Object> properties) {
        this.name = name;
        this.location = location;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Resource{" + "name='" + name + '\'' + ", location='" + location + '\'' + ", properties=" + properties
                + '}';
    }

}
