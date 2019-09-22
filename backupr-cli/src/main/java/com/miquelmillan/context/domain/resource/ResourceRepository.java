package com.miquelmillan.context.domain.resource;

import java.io.IOException;

public interface ResourceRepository {
    ResourceResult query(String path) throws IOException;
    ResourceResult store(Resource item) throws IOException, ResourceRepositoryException;
}