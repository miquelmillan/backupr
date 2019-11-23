package com.miquelmillan.infrastructure.filesystem;

import com.miquelmillan.context.domain.contents.Contents;
import com.miquelmillan.context.domain.location.Location;
import com.miquelmillan.context.domain.resource.Resource;
import com.miquelmillan.context.domain.resource.ResourceRepository;
import com.miquelmillan.context.domain.resource.ResourceRepositoryException;
import com.miquelmillan.context.domain.resource.ResourceResult;
import com.miquelmillan.context.infrastructure.filesystem.FileSystemResourceRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;


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
        String path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();

        ResourceRepository repo = new FileSystemResourceRepository();
        ResourceResult result = repo.query(path);

        assertSame(5, result.getResources().size());
        result.getResources().forEach((resource, _path) -> {
            System.out.println(resource);
            System.out.println(_path.getLocation());
        });
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

        ResourceResult result = repo.store(res);
        assertNotNull(result);

        for (Resource resource : result.getResources().values()) {
            assertNotNull(resource);
            assertSame(resource.getLocation().getLocation(), "filesystem/file1.txt");
            assertTrue(new File(path).exists());

            StringBuilder textBuilder = new StringBuilder();
            try (Reader reader = new BufferedReader(new InputStreamReader
                    (new FileInputStream(new File(path)),
                            Charset.forName(StandardCharsets.UTF_8.name())))) {
                int c;
                while ((c = reader.read()) != -1) {
                    textBuilder.append((char) c);
                }
            }

            assertEquals("Hello there!", textBuilder.toString());
        }

    }
}
