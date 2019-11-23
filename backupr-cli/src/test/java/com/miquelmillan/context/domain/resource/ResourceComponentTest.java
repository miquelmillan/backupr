package com.miquelmillan.context.domain.resource;

import com.miquelmillan.context.domain.contents.Contents;
import com.miquelmillan.context.domain.location.Location;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ResourceComponentTest {
    @Mock
    ResourceProcessor processor;

    @Mock
    ResourceRequester requester;

    String path;
    Location location;
    Resource sample;

    @Before
    public void setup() throws FileNotFoundException {
        MockitoAnnotations.initMocks(this);
        this.path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();
        this.location = new Location(path);
        this.sample = new Resource("file1.txt",
                new Location(path + File.separatorChar + "file1.txt"),
                new Contents(path + File.separatorChar + "file1.txt"));
    }

    @Test
    public void shouldProcessResource() throws IOException, ResourceRepositoryException {
        when(requester.listLocation(this.location)).thenReturn(this.prepareResourceResult());
        when(processor.storeResource(this.sample)).thenReturn(this.prepareResourceResult());

        ResourceComponent component = new ResourceComponent(requester, processor);
        component.storeLocation(this.location);

        verify(requester, times(1)).listLocation(this.location);
        verify(processor, times(1)).storeResource(this.sample);
    }

    private ResourceResult prepareResourceResult() throws FileNotFoundException {
        ResourceResult result = new ResourceResult();
        Map<String, Resource> resources = new HashMap();

        resources.put("file1.txt",
                new Resource("file1.txt",
                        new Location(path + File.separatorChar + "file1.txt"),
                        new Contents(path + File.separatorChar + "file1.txt" )));

        result.setResources(resources);
        return result;
    }
}
