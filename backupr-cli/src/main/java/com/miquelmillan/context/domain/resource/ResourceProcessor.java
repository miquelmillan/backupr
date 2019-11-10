package com.miquelmillan.context.domain.resource;

import java.io.IOException;

public class ResourceProcessor {
    private ResourceRepository repository;

    public ResourceProcessor(ResourceRepository repository){
        this.repository = repository;
    }

    public ResourceResult storeResource(Resource resource) throws IOException, ResourceRepositoryException {
        return this.repository.store(resource);
    }
}
