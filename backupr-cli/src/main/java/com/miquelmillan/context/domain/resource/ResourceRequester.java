package com.miquelmillan.context.domain.resource;

import com.miquelmillan.context.domain.location.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResourceRequester {
    private ResourceRepository repository;

    @Autowired
    public ResourceRequester(@Qualifier("fsResourceRepository") ResourceRepository repository){
        this.repository = repository;
    }

    public ResourceResult listLocation(Location location) throws IOException {
        return this.repository.query(location.getLocation());
    }

}
