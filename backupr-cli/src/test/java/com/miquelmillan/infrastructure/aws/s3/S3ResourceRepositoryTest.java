package com.miquelmillan.infrastructure.aws.s3;

import com.miquelmillan.context.domain.resource.Resource;
import com.miquelmillan.context.domain.resource.ResourceRepository;
import com.miquelmillan.context.domain.resource.ResourceRepositoryException;
import com.miquelmillan.context.infrastructure.aws.s3.S3ResourceRepository;
import com.miquelmillan.context.infrastructure.filesystem.resource.FileSystemResourceRepository;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.Assert.*;


public class S3ResourceRepositoryTest {
    @Test
    public void pathWithFiles_StorePath_StoreOk() throws IOException, ResourceRepositoryException {
        String path = ResourceRepository.class.getClassLoader().getResource("filesystem"+
                File.separator + "file1.txt" ).getPath();

        ResourceRepository fsRepo = new FileSystemResourceRepository();
        ResourceRepository s3Repo = new S3ResourceRepository("backupr-dev");

        Resource result = fsRepo.query(path);

        assertNotNull(result);
        s3Repo.store(result);
        Map<String, Object> props = result.getProperties();
        assertNotNull(
                props.get(Resource.Properties.MD5.toString())
        );
    }

    @Test
    public void pathWithFiles_QueryPath_QueryOk() throws IOException, ResourceRepositoryException {
        String path = ResourceRepository.class.getClassLoader().getResource("filesystem"+
                File.separator + "file1.txt" ).getPath();

        ResourceRepository s3Repo = new S3ResourceRepository("backupr-dev");

        Resource result = s3Repo.query(path);

        assertNotNull(result);
        assertSame(result.getLocation().getLocation(), path);
        assertNotNull(result.getContents());
        assertNotNull(result.getContents().getInputStream());

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (result.getContents().getInputStream(),
                        Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }

        assertEquals("hello!", textBuilder.toString());
    }
}
