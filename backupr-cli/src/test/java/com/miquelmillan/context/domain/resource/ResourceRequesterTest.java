package com.miquelmillan.context.domain.resource;

import com.miquelmillan.context.domain.location.Location;
import com.miquelmillan.context.infrastructure.filesystem.FileSystemResourceRepository;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;


public class ResourceRequesterTest {

    @Test
    public void shouldListLocation() throws IOException {
        String path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();

        ResourceRequester requester = new ResourceRequester(new FileSystemResourceRepository());
        ResourceResult result = requester.listLocation(new Location(path));

        assertNotNull(result);
        assertEquals(4, result.getResources().size());
    }
}
