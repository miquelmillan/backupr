package com.miquelmillan.backupr.uc.port;

import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.domain.resource.exception.ResourceRepositoryException;

import java.io.IOException;

public interface ResourceRepository {
    Resource query(Resource item) throws IOException;
    void store(Resource item) throws IOException, ResourceRepositoryException;
}