package com.miquelmillan.backupr.uc.local;

import com.miquelmillan.backupr.domain.location.Location;
import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.uc.UseCase;
import com.miquelmillan.backupr.uc.port.watcher.ResourceWatcher;

import java.io.File;
import java.io.IOException;

public class WatchFolder implements UseCase {
    private ResourceWatcher watcher;

    public void watch(File file) throws IOException {
        if (file.exists() && file.isDirectory()){
            watcher.watch(new Resource(new Location(file.getPath())));
        }
    }
}
