package com.miquelmillan.backupr.domain.resource;

import java.util.Objects;

public class ResourceEvent {
    private Resource resource;
    private EventType type;

    public enum EventType {
        CREATED, DELETED, UPDATED
    }

    public ResourceEvent(){}

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceEvent that = (ResourceEvent) o;
        return Objects.equals(resource, that.resource) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource, type);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResourceEvent{");
        sb.append("resource=").append(resource);
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
