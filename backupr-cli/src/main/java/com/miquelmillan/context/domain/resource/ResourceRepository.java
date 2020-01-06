package com.miquelmillan.context.domain.resource;

import java.io.IOException;
import java.util.UUID;

public interface ResourceRepository {
    Resource query(Resource item) throws IOException;
    void store(Resource item) throws IOException, ResourceRepositoryException;
}