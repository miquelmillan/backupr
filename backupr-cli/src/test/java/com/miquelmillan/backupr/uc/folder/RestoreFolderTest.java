package com.miquelmillan.backupr.uc.folder;

import com.miquelmillan.backupr.domain.contents.Contents;
import com.miquelmillan.backupr.domain.index.IndexEntry;
import com.miquelmillan.backupr.domain.index.IndexEntryRepository;
import com.miquelmillan.backupr.domain.location.Location;
import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.domain.resource.ResourceRepository;
import com.miquelmillan.backupr.domain.resource.exception.ResourceRepositoryException;
import com.miquelmillan.backupr.domain.resource.exception.ResourceUnavailableException;
import com.miquelmillan.backupr.domain.resource.exception.ResourceUnknownException;
import com.miquelmillan.backupr.uc.port.folder.RestoreFolder;
import com.miquelmillan.backupr.uc.port.port.ResourceComponent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


public class RestoreFolderTest {
    @Mock
    ResourceComponent component;

    @Mock
    IndexEntryRepository<Resource> index;

    String path;
    File location;
    Resource sample;
    UUID uid;

    @Before
    public void setup() throws FileNotFoundException {
        MockitoAnnotations.initMocks(this);
        this.path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();
        this.location = new File(path);
        this.uid = UUID.randomUUID();
        this.sample = new Resource(
                this.uid,
                "file1.txt",
                new Location(path + File.separatorChar + "file1.txt"),
                new Contents(path + File.separatorChar + "file1.txt"));
    }

    @Test
    public void restoreFolderUseCase_restoreFolder_backupOk() throws IOException,
            ResourceRepositoryException,
            ResourceUnknownException,
            ResourceUnavailableException {
        // Mock component outboundUid to return always ok
        // Prepare an index of 5 elements
        // Check all of them are processed correctly
        when(index.get(any(Resource.class))).thenReturn(this.prepareListIndexResult());
        doNothing().when(component).inboundResource(any(UUID.class));

        RestoreFolder restoreFolderUc = new RestoreFolder(component, index);

        assertTrue(restoreFolderUc.restoreFolder(this.location));

        // Mock component outboundUid to return always failure
        // Prepare an index of 5 elements
        // Check all of them are processed incorrectly
        when(index.get(any(Resource.class))).thenReturn(this.prepareListIndexResult());
        Mockito.doThrow(ResourceUnavailableException.class).when(component).outboundResource(any(UUID.class));

        assertFalse(restoreFolderUc.restoreFolder(this.location));

        verify(component, times(10)).outboundResource(any(UUID.class));
    }

    private IndexEntry<Resource> prepareIndexResultUnique() throws FileNotFoundException {
        return new IndexEntry(prepareResourceResultUnique());
    }

    private Resource prepareResourceResultUnique() throws FileNotFoundException {
        Resource result;

        result = new Resource(this.uid,
                "file1.txt",
                new Location(path + File.separatorChar + "file1.txt"),
                new Contents(path + File.separatorChar + "file1.txt" ));

        return result;
    }

    private List<Resource> prepareListIndexResult() throws FileNotFoundException {
        List<Resource> result = new ArrayList<>();

        result.add(new Resource(this.uid,
                "file1.txt",
                new Location(path + File.separatorChar + "file1.txt"),
                new Contents(path + File.separatorChar + "file1.txt" )));

        result.add(new Resource(this.uid,
                "file1.txt",
                new Location(path + File.separatorChar + "file1.txt"),
                new Contents(path + File.separatorChar + "file1.txt" )));

        result.add(new Resource(this.uid,
                "file1.txt",
                new Location(path + File.separatorChar + "file1.txt"),
                new Contents(path + File.separatorChar + "file1.txt" )));

        result.add(new Resource(this.uid,
                "file1.txt",
                new Location(path + File.separatorChar + "file1.txt"),
                new Contents(path + File.separatorChar + "file1.txt" )));

        result.add(new Resource(this.uid,
                "file1.txt",
                new Location(path + File.separatorChar + "file1.txt"),
                new Contents(path + File.separatorChar + "file1.txt" )));

        return result;
    }

}
