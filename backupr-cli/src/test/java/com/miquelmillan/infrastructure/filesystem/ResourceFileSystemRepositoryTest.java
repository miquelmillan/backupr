package com.miquelmillan.infrastructure.filesystem;

import static org.junit.Assert.assertSame;
import com.miquelmillan.context.domain.resource.ResourceRepository;
import com.miquelmillan.context.domain.resource.ResourceResult;
import com.miquelmillan.context.infrastructure.filesystem.ResourceFileSystemRepository;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.Before;

import java.io.IOException;
import java.nio.file.Path;

public class ResourceFileSystemRepositoryTest {

    @BeforeClass
    public static void setup() {
        System.out.println("@BeforeClass - executes once before all test methods in this class");
    }


    @Before
    public void init() {
        System.out.println("@BeforeEach - executes before each test method in this class");

    }


    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldListDirectoryContents() throws IOException {
        String path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();


        ResourceRepository repo = new ResourceFileSystemRepository();
        ResourceResult result = repo.query(path);

        assertSame(4, result.getResources().size());
        result.getResources().forEach((resource, _path) -> {
            System.out.println(resource);
            System.out.println(((Path) _path).toFile().length());
        });
    }
}
