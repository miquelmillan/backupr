package com.miquelmillan.backupr.uc.port;

import com.miquelmillan.backupr.domain.contents.Contents;
import com.miquelmillan.backupr.domain.location.Location;
import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.domain.resource.ResourceRepository;
import com.miquelmillan.backupr.domain.resource.exception.ResourceRepositoryException;
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

        LocalResourceProcessor processor = new LocalResourceProcessor(localRepo, remoteRepo);
        Resource result = processor.processOutputResource(sampleResource);

        verify(remoteRepo, times(1)).store(sampleResource);
        verify(localRepo, times(0)).store(sampleResource);

        assertEquals(result.getLocation(), new Location(path + File.separatorChar + "file1.txt"));
    }

    @Test
    public void mockedLocalRepo_StoreLocalFiles_StoreOk() throws IOException, ResourceRepositoryException {
        doNothing().when(localRepo).store(sampleResource);

        LocalResourceProcessor processor = new LocalResourceProcessor(localRepo, remoteRepo);
        Resource result = processor.processInputResource(sampleResource);

        verify(remoteRepo, times(0)).store(sampleResource);
        verify(localRepo, times(1)).store(sampleResource);

        assertEquals(result.getLocation(), new Location(path + File.separatorChar + "file1.txt"));
    }
}
