package com.miquelmillan.context.domain.resource;

import java.io.IOException;

public class ResourceRequester {
    private ResourceRepository repository;

    public ResourceRequester(ResourceRepository repository){
        this.repository = repository;
    }

    public ResourceResult listLocation(Location location) throws IOException {
        return this.repository.query(location.getLocation());
    }

}
