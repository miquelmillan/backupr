package com.miquelmillan.backupr.uc.folder;

import com.miquelmillan.backupr.domain.contents.Contents;
import com.miquelmillan.backupr.domain.index.IndexEntry;
import com.miquelmillan.backupr.domain.index.IndexEntryRepository;
import com.miquelmillan.backupr.domain.location.Location;
import com.miquelmillan.backupr.uc.port.Resource;
import com.miquelmillan.backupr.uc.port.ResourceRepository;
import com.miquelmillan.backupr.uc.port.exception.ResourceRepositoryException;
import com.miquelmillan.backupr.uc.port.exception.ResourceUnavailableException;
import com.miquelmillan.backupr.uc.port.exception.ResourceUnknownException;
import com.miquelmillan.backupr.uc.port.folder.IndexFolder;
import com.miquelmillan.backupr.uc.port.port.ResourceComponent;
import com.miquelmillan.backupr.uc.port.port.ResourceProcessor;
import com.miquelmillan.backupr.uc.port.port.ResourceRequester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


public class IndexFolderTest {
    @Mock
    IndexEntryRepository<Resource> index;

    File path;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.path = new File(ResourceRepository.class.getClassLoader().getResource("filesystem").getFile());
    }

    @Test
    public void IndexFolderUseCase_indexFolder_backupOk() throws IOException {
        doNothing().when(index).addOrUpdate(any(List.class));

        IndexFolder indexFolder = new IndexFolder(index);
        indexFolder.indexFolder(this.path);

        verify(index, times(1)).addOrUpdate(any(List.class));
    }
}
