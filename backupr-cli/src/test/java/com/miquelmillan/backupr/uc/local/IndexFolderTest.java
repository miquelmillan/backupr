package com.miquelmillan.backupr.uc.local;

import com.miquelmillan.backupr.domain.index.IndexEntryRepository;
import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.domain.resource.ResourceRepository;
import com.miquelmillan.backupr.uc.local.IndexFolder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
