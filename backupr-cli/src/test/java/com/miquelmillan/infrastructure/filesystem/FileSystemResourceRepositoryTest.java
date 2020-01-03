package com.miquelmillan.infrastructure.filesystem;

import com.miquelmillan.context.domain.contents.Contents;
import com.miquelmillan.context.domain.location.Location;
import com.miquelmillan.context.domain.resource.Resource;
import com.miquelmillan.context.domain.resource.ResourceRepository;
import com.miquelmillan.context.domain.resource.ResourceRepositoryException;
import com.miquelmillan.context.infrastructure.filesystem.resource.FileSystemResourceRepository;
import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

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
        Resource result = repo.query(path);

        assertNotNull(result);
    }

    @Test
    public void resourceWithContents_StorePath_StoreOk() throws IOException, ResourceRepositoryException {
        String path = ResourceRepository.class.getClassLoader().getResource("").getPath()
                + "filesystem" + File.separator + "recovered.txt";

        ResourceRepository repo = new FileSystemResourceRepository();
        Resource res = new Resource(
                "recovered.txt",
                new Location(path),
                new Contents(new ByteArrayInputStream("Hello there!".getBytes()))
        );

        repo.store(res);
    }
}
