package com.miquelmillan.context.domain.resource;

import java.io.IOException;

public interface ResourceRepository {
    Resource query(String path) throws IOException;
    void store(Resource item) throws IOException, ResourceRepositoryException;
}