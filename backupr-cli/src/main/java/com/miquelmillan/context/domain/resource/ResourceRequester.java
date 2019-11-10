package com.miquelmillan.context.domain.resource;

import com.miquelmillan.context.domain.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

public class ResourceRequester {
    @Autowired
    @Qualifier("s3ResourceRepository")
    private ResourceRepository repository;

    public ResourceRequester(ResourceRepository repository){
        this.repository = repository;
    }

    public ResourceResult listLocation(Location location) throws IOException {
        return this.repository.query(location.getLocation());
    }

}
