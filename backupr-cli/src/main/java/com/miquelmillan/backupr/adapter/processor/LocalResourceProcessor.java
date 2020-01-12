package com.miquelmillan.backupr.adapter.processor;

import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.domain.resource.exception.ResourceRepositoryException;
import com.miquelmillan.backupr.uc.port.ResourceProcessor;
import com.miquelmillan.backupr.uc.port.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LocalResourceProcessor implements ResourceProcessor {
    private ResourceRepository localRepository;
    private ResourceRepository remoteRepository;

    @Autowired
    public LocalResourceProcessor(
            @Qualifier("fsResourceRepository") ResourceRepository localRepository,
            @Qualifier("s3ResourceRepository") ResourceRepository remoteRepository){
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }

    public Resource processOutputResource(Resource resource) throws IOException, ResourceRepositoryException {
        this.remoteRepository.store(resource);
        return resource;
    }

    public Resource processInputResource(Resource resource) throws IOException, ResourceRepositoryException {
        this.localRepository.store(resource);
        return resource;
    }
}
