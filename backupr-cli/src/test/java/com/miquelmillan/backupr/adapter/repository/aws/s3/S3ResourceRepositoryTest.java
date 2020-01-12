package com.miquelmillan.backupr.adapter.repository.aws.s3;

import com.miquelmillan.backupr.domain.location.Location;
import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.uc.port.repository.ResourceRepository;
import com.miquelmillan.backupr.domain.resource.exception.ResourceRepositoryException;
import com.miquelmillan.backupr.adapter.repository.aws.s3.S3ResourceRepository;
import com.miquelmillan.backupr.adapter.repository.filesystem.FileSystemResourceRepository;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;


public class S3ResourceRepositoryTest {
    @Test
    public void pathWithFiles_StorePath_StoreOkAndQueryOk() throws IOException, ResourceRepositoryException {
        String path = ResourceRepository.class.getClassLoader().getResource("filesystem"+
                File.separator + "file1.txt" ).getPath();

        Resource sample = new Resource(
                UUID.nameUUIDFromBytes("file1.txt".getBytes()),
                "file1.txt",
                new Location(path),
                null
        );

        ResourceRepository fsRepo = new FileSystemResourceRepository();
        ResourceRepository s3Repo = new S3ResourceRepository("backupr-dev");

        Resource result = fsRepo.query(sample);

        assertNotNull(result);
        s3Repo.store(result);
        Map<String, Object> props = result.getProperties();
        assertNotNull(
                props.get(Resource.Properties.MD5.toString())
        );

        result = s3Repo.query(sample);

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
