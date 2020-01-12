package com.miquelmillan.backupr.uc.port.processor;

import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.domain.resource.exception.ResourceRepositoryException;

import java.io.IOException;

public interface ResourceProcessor {
    Resource processOutputResource(Resource resource) throws IOException, ResourceRepositoryException;
    Resource processInputResource(Resource resource) throws IOException, ResourceRepositoryException;
}
