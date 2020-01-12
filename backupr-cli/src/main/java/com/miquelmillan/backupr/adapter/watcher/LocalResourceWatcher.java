package com.miquelmillan.backupr.adapter.watcher;

import com.miquelmillan.backupr.adapter.watcher.filesystem.FolderWatcher;
import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.uc.port.watcher.ResourceWatcher;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class LocalResourceWatcher implements ResourceWatcher, Observer {
    FolderWatcher watcher;

    public LocalResourceWatcher() {
    }

    @Override
    public void watch(Resource resource) throws IOException {
        watcher = new FolderWatcher((new File(resource.getLocation().getLocation())).toPath());
        watcher.processEvents();
    }


    @Override
    public void update(Observable o, Object arg) {
        // if file is created, updated => Call the processOutputResource

        // if file is deleted => Update index? :-/
    }
}


