package com.miquelmillan.context.domain.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miquelmillan.context.domain.contents.Contents;
import com.miquelmillan.context.domain.location.Location;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Resource implements Serializable {
    public enum Properties {
        MD5
    }

    private UUID id;
    private String name;
    private Location location;
    @JsonIgnore
    private Contents contents;
    private Map<String, Object> properties;

    public Resource(){
    }

    public Resource(Location loc){
        this.name = "";
        this.location = loc;
    }

    public Resource(String name, Location location, Contents contents) {
        this.name = name;
        this.location = location;
        this.contents = contents;
    }

    public Resource(UUID id, String name, Location location, Contents contents) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.contents = contents;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Contents getContents() {
        return contents;
    }

    public void setContents(Contents contents) {
        this.contents = contents;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return id.equals(resource.id) &&
                name.equals(resource.name) &&
                location.equals(resource.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, contents, properties);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Resource{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", location=").append(location);
        sb.append('}');
        return sb.toString();
    }
}