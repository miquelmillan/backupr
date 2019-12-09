package com.miquelmillan.infrastructure.aws.s3;

import com.miquelmillan.context.domain.resource.Resource;
import com.miquelmillan.context.domain.resource.ResourceRepository;
import com.miquelmillan.context.domain.resource.ResourceRepositoryException;
import com.miquelmillan.context.domain.resource.ResourceResult;
import com.miquelmillan.context.infrastructure.aws.s3.S3ResourceRepository;
import com.miquelmillan.context.infrastructure.filesystem.resource.FileSystemResourceRepository;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;


public class S3ResourceRepositoryTest {
    @Test
    public void pathWithFiles_StorePath_StoreOk() throws IOException, ResourceRepositoryException {
        String path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();

        ResourceRepository fsRepo = new FileSystemResourceRepository();
        ResourceRepository s3Repo = new S3ResourceRepository("backupr-dev");

        ResourceResult result = fsRepo.query(path);

        assertSame(5, result.getResources().size());

        for (Resource resource: result.getResources().values()){
            assertNotNull(resource);
            ResourceResult res = s3Repo.store(resource);
            Map<String, Object> props = res.getResources().get(resource.getName()).getProperties();
            assertNotNull(
                    props.get(Resource.Properties.MD5.toString())
            );
        }
    }

    @Test
    public void pathWithFiles_QueryPath_QueryOk() throws IOException, ResourceRepositoryException {
        String path = "filesystem/file1.txt";

        ResourceRepository s3Repo = new S3ResourceRepository("backupr-dev");
        ResourceResult result = s3Repo.query(path);

        assertNotNull(result);

        for (Resource resource: result.getResources().values()){
            assertNotNull(resource);
            assertSame(resource.getLocation().getLocation(), "filesystem/file1.txt");
            assertNotNull(resource.getContents());
            assertNotNull(resource.getContents().getInputStream());

            StringBuilder textBuilder = new StringBuilder();
            try (Reader reader = new BufferedReader(new InputStreamReader
                    (resource.getContents().getInputStream(),
                            Charset.forName(StandardCharsets.UTF_8.name())))) {
                int c;
                while ((c = reader.read()) != -1) {
                    textBuilder.append((char) c);
                }
            }

            assertEquals("hello!", textBuilder.toString());
        }
    }
}
