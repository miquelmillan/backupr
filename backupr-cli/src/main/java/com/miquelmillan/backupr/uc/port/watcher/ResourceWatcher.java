package com.miquelmillan.backupr.uc.port.watcher;

import com.miquelmillan.backupr.domain.resource.Resource;

import java.io.IOException;

public interface ResourceWatcher {
    void watch(Resource resource) throws IOException;
}
