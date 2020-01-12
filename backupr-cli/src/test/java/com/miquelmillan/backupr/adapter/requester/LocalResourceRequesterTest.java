package com.miquelmillan.backupr.adapter.requester;

import com.miquelmillan.backupr.domain.contents.Contents;
import com.miquelmillan.backupr.domain.location.Location;
import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.uc.port.repository.ResourceRepository;
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


public class LocalResourceRequesterTest {
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
                UUID.randomUUID(),
                "file1",
                new Location(this.path + File.separatorChar + "file1.txt"),
                new Contents(this.path + File.separatorChar + "file1.txt"));
    }

    @Test
    public void mockedLocalRepo_ListLocalFiles_ListOk() throws IOException {
        when(localRepo.query(this.sampleResource)).thenReturn(this.prepareResourceResult());

        LocalResourceRequester requester = new LocalResourceRequester(localRepo, remoteRepo);
        Resource result = requester.requestOutputResource(this.sampleResource);

        verify(localRepo, times(1)).query(this.sampleResource);
        verify(remoteRepo, times(0)).query(this.sampleResource);

        assertEquals(result.getLocation(),
                                new Location(path + File.separatorChar + "file1.txt"));
    }


    private Resource prepareResourceResult() throws FileNotFoundException {
        Resource result;

        result = new Resource(
                UUID.randomUUID(),
                "file1.txt",
                new Location(path + File.separatorChar + "file1.txt"),
                new Contents(path + File.separatorChar + "file1.txt"));

        return result;
    }
}
