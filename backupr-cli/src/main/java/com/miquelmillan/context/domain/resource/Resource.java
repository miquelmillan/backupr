package com.miquelmillan.context.domain.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miquelmillan.context.domain.contents.Contents;
import com.miquelmillan.context.domain.location.Location;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class Resource implements Serializable {
    public enum Properties {
        MD5
    }

    private String name;
    private Location location;
    @JsonIgnore
    private Contents contents;
    private Map<String, Object> properties;

    public Resource(String name, Location location, Contents contents) {
        this.name = name;
        this.location = location;
        this.contents = contents;
    }


    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public Contents getContents() {
        return contents;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Resource{");
        sb.append("name='").append(name).append('\'');
        sb.append(", location=").append(location);
        sb.append(", contents=").append(contents);
        sb.append(", properties=").append(properties);
        sb.append('}');
        return sb.toString();
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
        return Objects.hash(name, location, contents, properties);
    }

}