package com.miquelmillan.context.domain.resource;

import com.miquelmillan.context.domain.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResourceRequester {
    private ResourceRepository localRepository;
    private ResourceRepository remoteRepository;

    @Autowired
    public ResourceRequester(
            @Qualifier("fsResourceRepository") ResourceRepository localRepository,
            @Qualifier("s3ResourceRepository") ResourceRepository remoteRepository
    ){
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }

    public Resource requestOutputResource(String path) throws IOException {
        return this.localRepository.query(path);
    }

    public Resource requestInputResource(String path) throws IOException {
        return this.remoteRepository.query(path);
    }

    public Resource requestOutputLocation(Location location) throws IOException {
        return this.localRepository.query(location.getLocation());
    }

    public Resource requestInputLocation(Location location) throws IOException {
        return this.remoteRepository.query(location.getLocation());
    }
}
