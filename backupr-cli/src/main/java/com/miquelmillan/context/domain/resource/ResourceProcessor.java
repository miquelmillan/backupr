package com.miquelmillan.context.domain.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResourceProcessor {
    private ResourceRepository repository;

    @Autowired
    public ResourceProcessor(@Qualifier("s3ResourceRepository") ResourceRepository repository){
        this.repository = repository;
    }

    public ResourceResult storeResource(Resource resource) throws IOException, ResourceRepositoryException {
        return this.repository.store(resource);
    }
}
