package com.miquelmillan.context.domain.resource;

import java.util.Map;
import java.util.Objects;

public class Resource {

    public enum Properties {
        MD5
    };

    private String name;
    private Location location;
    private Map<String, Object> properties;

    public Resource(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public Resource(String name, Location location, Map<String, Object> properties) {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(name, resource.name) &&
                Objects.equals(location, resource.location) &&
                Objects.equals(properties, resource.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, properties);
    }
}
