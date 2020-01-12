package com.miquelmillan.backupr.adapter.watcher;

import com.miquelmillan.backupr.uc.port.repository.ResourceRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;


public class FileSystemFolderWatcherTest {

    private String basePath;

    @Before
    public void setUp() {
        this.basePath = ResourceRepository.class.getClassLoader().getResource("filesystem").getFile();
    }

    @After
    public void tearDown() {
        return;
    }

    @Test
    public void pathWithFiles_WatchFolder_WatchOk() throws IOException {
        // FolderWatcher watcher = new FolderWatcher((new File(this.basePath)).toPath(), true);
        // watcher.processEvents();
    }
}
