package com.miquelmillan.context.domain.resource;

import java.util.Map;

public class ResourceResult {

    private Map<String, Resource> resources;

    public ResourceResult() {}

    public ResourceResult(Map<String, Resource> resources) {
        this.resources = resources;
    }

    public Map<String, Resource> getResources() {
        return resources;
    }

    public void setResources(Map<String, Resource> resources) {
        this.resources = resources;
    }
}
