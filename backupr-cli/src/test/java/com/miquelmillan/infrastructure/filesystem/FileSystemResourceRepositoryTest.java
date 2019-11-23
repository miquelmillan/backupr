package com.miquelmillan.infrastructure.filesystem;

import com.miquelmillan.context.domain.resource.ResourceRepository;
import com.miquelmillan.context.domain.resource.ResourceResult;
import com.miquelmillan.context.infrastructure.filesystem.FileSystemResourceRepository;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertSame;


public class FileSystemResourceRepositoryTest {

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
}
