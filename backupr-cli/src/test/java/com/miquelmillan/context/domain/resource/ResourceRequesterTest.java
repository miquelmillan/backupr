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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class ResourceRequesterTest {
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
    public void mockedLocalRepo_ListLocalFiles_ListOk() throws IOException {
        when(localRepo.query(this.path)).thenReturn(this.prepareResourceResult());

        ResourceRequester requester = new ResourceRequester(localRepo, remoteRepo);
        Resource result = requester.requestOutputLocation(new Location(this.path));

        verify(localRepo, times(1)).query(this.path);
        verify(remoteRepo, times(0)).query(this.path);

        assertEquals(result.getLocation(),
                                new Location(path + File.separatorChar + "file1.txt"));
    }

    @Test
    public void mockedRemoteRepo_ListRemoteFiles_ListOk() throws IOException {
        when(remoteRepo.query(this.path)).thenReturn(this.prepareResourceResult());

        ResourceRequester requester = new ResourceRequester(localRepo, remoteRepo);
        Resource result = requester.requestInputLocation(new Location(this.path));

        verify(remoteRepo, times(1)).query(this.path);
        verify(localRepo, times(0)).query(this.path);

        assertEquals(result.getLocation(),
                new Location(path + File.separatorChar + "file1.txt"));
    }

    private Resource prepareResourceResult() throws FileNotFoundException {
        Resource result;

        result = new Resource("file1.txt",
                new Location(path + File.separatorChar + "file1.txt"),
                new Contents(path + File.separatorChar + "file1.txt"));

        return result;
    }
}
