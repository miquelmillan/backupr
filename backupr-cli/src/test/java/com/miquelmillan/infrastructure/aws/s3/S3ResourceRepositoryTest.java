package com.miquelmillan.infrastructure.aws.s3;

import com.miquelmillan.context.domain.resource.Resource;
import com.miquelmillan.context.domain.resource.ResourceRepository;
import com.miquelmillan.context.domain.resource.ResourceRepositoryException;
import com.miquelmillan.context.domain.resource.ResourceResult;
import com.miquelmillan.context.infrastructure.aws.s3.S3ResourceRepository;
import com.miquelmillan.context.infrastructure.filesystem.FileSystemResourceRepository;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;


public class S3ResourceRepositoryTest {
    @Test
    public void pathWithFiles_StorePath_StoreOk() throws IOException, ResourceRepositoryException {
        String path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();


        ResourceRepository fsRepo = new FileSystemResourceRepository();
        ResourceRepository s3Repo = new S3ResourceRepository("backupr-dev");

        ResourceResult result = fsRepo.query(path);

        assertSame(4, result.getResources().size());

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
        String path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();

        ResourceRepository s3Repo = new S3ResourceRepository("backupr-dev");
        ResourceResult result = s3Repo.query(path);

        assertSame(4, result.getResources().size());

        for (Resource resource: result.getResources().values()){
            assertNotNull(resource);
        }
    }

}
