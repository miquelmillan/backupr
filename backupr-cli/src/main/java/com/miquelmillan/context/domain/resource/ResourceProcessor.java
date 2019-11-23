package com.miquelmillan.context.domain.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResourceProcessor {
    private ResourceRepository localRepository;
    private ResourceRepository remoteRepository;

    @Autowired
    public ResourceProcessor(
            @Qualifier("fsResourceRepository") ResourceRepository localRepository,
            @Qualifier("s3ResourceRepository") ResourceRepository remoteRepository){
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }

    public ResourceResult processOutputResource(Resource resource) throws IOException, ResourceRepositoryException {
        return this.remoteRepository.store(resource);
    }

    public ResourceResult processInputResource(Resource resource) throws IOException, ResourceRepositoryException {
        return this.localRepository.store(resource);
    }
}
