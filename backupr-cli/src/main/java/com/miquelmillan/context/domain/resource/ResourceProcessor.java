package com.miquelmillan.context.domain.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

public class ResourceProcessor {
    @Autowired
    @Qualifier("s3ResourceRepository")
    private ResourceRepository repository;

    public ResourceProcessor(ResourceRepository repository){
        this.repository = repository;
    }

    public ResourceResult storeResource(Resource resource) throws IOException, ResourceRepositoryException {
        return this.repository.store(resource);
    }
}
