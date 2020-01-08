package com.miquelmillan.backupr.uc.port.port;

import com.miquelmillan.backupr.uc.port.Resource;
import com.miquelmillan.backupr.uc.port.ResourceRepository;
import com.miquelmillan.backupr.uc.port.exception.ResourceRepositoryException;
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

    public Resource processOutputResource(Resource resource) throws IOException, ResourceRepositoryException {
        this.remoteRepository.store(resource);
        return resource;
    }

    public Resource processInputResource(Resource resource) throws IOException, ResourceRepositoryException {
        this.localRepository.store(resource);
        return resource;
    }
}
