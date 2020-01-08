package com.miquelmillan.backupr.uc.port;

import com.miquelmillan.backupr.uc.port.exception.ResourceRepositoryException;

import java.io.IOException;

public interface ResourceRepository {
    Resource query(Resource item) throws IOException;
    void store(Resource item) throws IOException, ResourceRepositoryException;
}