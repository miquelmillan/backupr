package com.miquelmillan.backupr.adapter.observer;

import com.miquelmillan.backupr.uc.port.repository.ResourceRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;


public class FileSystemFolderObserverTest {

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
        // FolderObserver watcher = new FolderObserver((new File(this.basePath)).toPath(), true);
        // watcher.processEvents();
    }
}
