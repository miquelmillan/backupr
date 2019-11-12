package com.miquelmillan.context.domain.resource;

import com.miquelmillan.context.domain.location.Location;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ResourceComponentTest {
    @Mock
    ResourceProcessor processor;

    @Mock
    ResourceRequester requester;

    String path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();

    Location location = new Location(path);
    Resource storedResource =  new Resource("", new Location(path));
    Resource sample =  new Resource("file1", new Location(path + File.separatorChar + "file1"));

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldProcessResource() throws IOException, ResourceRepositoryException {
        when(requester.requestOutputLocation(location)).thenReturn(this.prepareResourceResult());
        when(processor.processOutputResource(sample)).thenReturn(this.prepareResourceResult());

        ResourceComponent component = new ResourceComponent(requester, processor);
        component.outboundLocation(location);

        verify(requester, times(1)).requestOutputLocation(location);
        verify(processor, times(1)).processOutputResource(sample);
    }

    @Test
    public void shouldRestoreResource() throws IOException, ResourceRepositoryException {
        when(requester.requestInputLocation(location)).thenReturn(this.prepareResourceResult());
        when(processor.processInputResource(sample)).thenReturn(this.prepareResourceResult());

        ResourceComponent component = new ResourceComponent(requester, processor);
        component.inboundLocation(location);

        verify(requester, times(1)).requestInputLocation(location);
        verify(processor, times(1)).processInputResource(sample);
    }

    private ResourceResult prepareResourceResult(){
        ResourceResult result = new ResourceResult();
        Map<String, Resource> resources = new HashMap();

        resources.put("file1", new Resource("file1", new Location(path + File.separatorChar + "file1")));

        result.setResources(resources);
        return result;
    }
}
