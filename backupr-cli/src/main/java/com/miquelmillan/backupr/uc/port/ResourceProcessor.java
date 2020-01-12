package com.miquelmillan.backupr.uc.port;

import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.domain.resource.exception.ResourceRepositoryException;

import java.io.IOException;

public interface ResourceProcessor {
    public Resource processOutputResource(Resource resource) throws IOException, ResourceRepositoryException;
    public Resource processInputResource(Resource resource) throws IOException, ResourceRepositoryException;
}
