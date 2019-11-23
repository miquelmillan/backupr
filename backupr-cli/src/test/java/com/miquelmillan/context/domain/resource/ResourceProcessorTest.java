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
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ResourceProcessorTest {
    @Mock
    ResourceRepository remoteRepo;

    @Mock
    ResourceRepository localRepo;

    String path;
    Resource sampleFile, samplePath;

    @Before
    public void setup() throws FileNotFoundException {
        MockitoAnnotations.initMocks(this);
        this.path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();
        this.sampleFile =  new Resource("file1",
                new Location(this.path + File.separatorChar + "file1.txt"),
                new Contents(this.path + File.separatorChar + "file1.txt"));
        this.samplePath =  new Resource("filesystem",
                new Location(this.path),
                null);

    }

    @Test
    public void mockedRemoteRepo_StoreRemoteFiles_StoreOk() throws IOException, ResourceRepositoryException {
        when(remoteRepo.store(sampleFile)).thenReturn(this.prepareResourceResult());

        ResourceProcessor processor = new ResourceProcessor(localRepo, remoteRepo);
        ResourceResult result = processor.processOutputResource(sampleFile);

        verify(remoteRepo, times(1)).store(sampleFile);
        verify(localRepo, times(0)).store(sampleFile);

        assertEquals(result.getResources().get("file1.txt").getLocation(), new Location(path + File.separatorChar + "file1.txt"));
    }

    @Test
    public void mockedLocalRepo_StoreLocalFiles_StoreOk() throws IOException, ResourceRepositoryException {
        when(localRepo.store(sampleFile)).thenReturn(this.prepareResourceResult());

        ResourceProcessor processor = new ResourceProcessor(localRepo, remoteRepo);
        ResourceResult result = processor.processInputResource(sampleFile);

        verify(remoteRepo, times(0)).store(sampleFile);
        verify(localRepo, times(1)).store(sampleFile);

        assertEquals(result.getResources().get("file1.txt").getLocation(), new Location(path + File.separatorChar + "file1.txt"));
    }


    private ResourceResult prepareResourceResult() throws FileNotFoundException {
        ResourceResult result = new ResourceResult();
        Map<String, Resource> resources = new HashMap();

        resources.put("file1.txt", new Resource("file1.txt",
                new Location(path + File.separatorChar + "file1.txt"),
                new Contents(path + File.separatorChar + "file1.txt")));

        result.setResources(resources);

        return result;
    }
}
