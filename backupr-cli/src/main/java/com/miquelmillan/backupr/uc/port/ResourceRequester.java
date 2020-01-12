package com.miquelmillan.backupr.uc.port;

import com.miquelmillan.backupr.domain.resource.Resource;

import java.io.IOException;

public interface ResourceRequester {
    public Resource requestOutputResource(Resource item) throws IOException;
    public Resource requestInputResource(Resource item) throws IOException;

}
