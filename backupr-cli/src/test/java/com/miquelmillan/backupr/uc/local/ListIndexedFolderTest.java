package com.miquelmillan.backupr.uc.local;

import com.miquelmillan.backupr.domain.contents.Contents;
import com.miquelmillan.backupr.domain.index.IndexEntry;
import com.miquelmillan.backupr.domain.index.IndexEntryRepository;
import com.miquelmillan.backupr.domain.location.Location;
import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.domain.resource.ResourceRepository;
import com.miquelmillan.backupr.uc.local.ListIndexedFolder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


public class ListIndexedFolderTest {
    @Mock
    IndexEntryRepository<Resource> index;

    String path;
    UUID uid;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();
        this.uid = UUID.randomUUID();
    }


    @Test
    public void listIndexedFolderUseCase_listFolder_backupOk() throws IOException {
        when(index.listAll()).thenReturn(Arrays.asList(new IndexEntry[] {prepareIndexResultUnique()}));

        ListIndexedFolder uc = new ListIndexedFolder(index);
        uc.listFolder();

        verify(index, times(1)).listAll();
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
}
