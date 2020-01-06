package com.miquelmillan.context.domain.resource;

import com.miquelmillan.context.domain.contents.Contents;
import com.miquelmillan.context.domain.location.Location;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ResourceProcessorTest {
    @Mock
    ResourceRepository remoteRepo;

    @Mock
    ResourceRepository localRepo;

    String path;
    Resource sampleResource;

    @Before
    public void setup() throws FileNotFoundException {
        MockitoAnnotations.initMocks(this);
        this.path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();
        this.sampleResource =  new Resource(
                UUID.nameUUIDFromBytes("file1.txt".getBytes()),
                "file1",
                new Location(this.path + File.separatorChar + "file1.txt"),
                new Contents(this.path + File.separatorChar + "file1.txt"));
    }

    @Test
    public void mockedRemoteRepo_StoreRemoteFiles_StoreOk() throws IOException, ResourceRepositoryException {
        doNothing().when(remoteRepo).store(sampleResource);

        ResourceProcessor processor = new ResourceProcessor(localRepo, remoteRepo);
        Resource result = processor.processOutputResource(sampleResource);

        verify(remoteRepo, times(1)).store(sampleResource);
        verify(localRepo, times(0)).store(sampleResource);

        assertEquals(result.getLocation(), new Location(path + File.separatorChar + "file1.txt"));
    }

    @Test
    public void mockedLocalRepo_StoreLocalFiles_StoreOk() throws IOException, ResourceRepositoryException {
        doNothing().when(localRepo).store(sampleResource);

        ResourceProcessor processor = new ResourceProcessor(localRepo, remoteRepo);
        Resource result = processor.processInputResource(sampleResource);

        verify(remoteRepo, times(0)).store(sampleResource);
        verify(localRepo, times(1)).store(sampleResource);

        assertEquals(result.getLocation(), new Location(path + File.separatorChar + "file1.txt"));
    }
}
