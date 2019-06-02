package com.miquelmillan.context.domain.resource;

import java.util.Map;

public class ResourceResult {

    private Map<String, Object> resources;

    public ResourceResult(Map<String, Object> resources) {
        this.resources = resources;
    }

    public Map<String, Object> getResources() {
        return resources;
    }

    public void setResources(Map<String, Object> resources) {
        this.resources = resources;
    }
}
