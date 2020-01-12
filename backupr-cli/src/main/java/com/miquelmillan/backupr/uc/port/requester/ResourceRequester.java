package com.miquelmillan.backupr.uc.port.requester;

import com.miquelmillan.backupr.domain.resource.Resource;

import java.io.IOException;

public interface ResourceRequester {
    Resource requestOutputResource(Resource item) throws IOException;
    Resource requestInputResource(Resource item) throws IOException;
}
