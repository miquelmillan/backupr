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
import static org.junit.Assert.*;

public class ResourceProcessorTest {
    @Mock
    ResourceRepository repo;

    String path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();
    Resource sample =  new Resource("file1",  new Location(path + File.separatorChar + "file1"));

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldStoreResources() throws IOException, ResourceRepositoryException {
        when(repo.store(sample)).thenReturn(this.prepareResourceResult());

        ResourceProcessor processor = new ResourceProcessor(repo);
        ResourceResult result = processor.processOutputResource(sample);

        verify(repo, times(1)).store(sample);
        assertEquals(result.getResources().get("file1").getLocation(), new Location(path + File.separatorChar + "file1"));
    }

    private ResourceResult prepareResourceResult(){
        ResourceResult result = new ResourceResult();
        Map<String, Resource> resources = new HashMap();

        resources.put("file1", new Resource("file1", new Location(path + File.separatorChar + "file1")));

        result.setResources(resources);
        return result;
    }
}
