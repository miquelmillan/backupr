package com.miquelmillan.backupr.adapter.filesystem;

import com.miquelmillan.backupr.domain.contents.Contents;
import com.miquelmillan.backupr.domain.location.Location;
import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.domain.resource.ResourceRepository;
import com.miquelmillan.backupr.domain.resource.exception.ResourceRepositoryException;
import com.miquelmillan.backupr.adapter.filesystem.resource.FileSystemResourceRepository;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;


public class FileSystemResourceRepositoryTest {

    @After
    public void tearDown() {
        // Cleanup created files
        File f = new File(
                ResourceRepository.class.getClassLoader().getResource("").getPath()
                                                        + "filesystem" + File.separator + "recovered.txt");
        if (f.exists()) {
            f.delete();
        }
    }

    @Test
    public void pathWithFiles_QueryPath_QueryOk() throws IOException {
        String path = ResourceRepository.class.getClassLoader().getResource("filesystem" +
                                    File.separator + "folder_a" +
                                    File.separator + "file1.txt").getPath();

        ResourceRepository repo = new FileSystemResourceRepository();
        Resource res = new Resource(
                UUID.nameUUIDFromBytes("recovered.txt".getBytes()),
                "recovered.txt",
                new Location(path),
                null
        );

        Resource result = repo.query(res);

        assertNotNull(result);
        assertNotNull(result.getContents());
    }

    @Test
    public void resourceWithContents_StorePath_StoreOk() throws IOException, ResourceRepositoryException {
        String path = ResourceRepository.class.getClassLoader().getResource("").getPath()
                + "filesystem" + File.separator + "recovered.txt";

        ResourceRepository repo = new FileSystemResourceRepository();
        Resource res = new Resource(
                UUID.nameUUIDFromBytes("recovered.txt".getBytes()),
                "recovered.txt",
                new Location(path),
                new Contents(new ByteArrayInputStream("Hello there!".getBytes()))
        );

        repo.store(res);
    }
}
