package com.miquelmillan.backupr.uc.local;

import com.miquelmillan.backupr.domain.contents.Contents;
import com.miquelmillan.backupr.domain.index.IndexEntry;
import com.miquelmillan.backupr.domain.location.Location;
import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.domain.resource.exception.ResourceRepositoryException;
import com.miquelmillan.backupr.domain.resource.exception.ResourceUnavailableException;
import com.miquelmillan.backupr.domain.resource.exception.ResourceUnknownException;
import com.miquelmillan.backupr.uc.port.repository.IndexEntryRepository;
import com.miquelmillan.backupr.uc.port.processor.ResourceProcessor;
import com.miquelmillan.backupr.uc.port.repository.ResourceRepository;
import com.miquelmillan.backupr.uc.port.requester.ResourceRequester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import static org.mockito.Mockito.*;


public class BackupFileTest {
    @Mock
    ResourceProcessor processor;

    @Mock
    ResourceRequester requester;

    @Mock
    IndexEntryRepository<Resource> index;

    String path;
    Resource sample;
    UUID uid;
    IndexEntry<Resource> entry;

    @Before
    public void setup() throws FileNotFoundException {
        MockitoAnnotations.initMocks(this);
        this.path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();
        this.uid = UUID.randomUUID();
        this.sample = new Resource(
                this.uid,
                "file1.txt",
                new Location(path + File.separatorChar + "file1.txt"),
                new Contents(path + File.separatorChar + "file1.txt"));
        this.entry = this.prepareIndexResultUnique();
    }
    @Test
    public void backupFileUseCase_backupFile_backupOk() throws IOException, ResourceRepositoryException, ResourceUnknownException, ResourceUnavailableException {
        when(requester.requestOutputResource(sample)).thenReturn(this.prepareResourceResultUnique());
        when(processor.processOutputResource(sample)).thenReturn(this.prepareResourceResultUnique());
        when(index.get(uid)).thenReturn(entry);
        doNothing().when(index).addOrUpdate(entry);
        doNothing().when(index).addOrUpdate(new IndexEntry(entry.getElement(), IndexEntry.State.STORED));


        BackupFile backupUC = new BackupFile(requester, processor, index);
        backupUC.backupFile(uid);

        verify(requester, times(1)).requestOutputResource(sample);
        verify(processor, times(1)).processOutputResource(sample);
        verify(index, times(1)).addOrUpdate(entry);
        verify(index, times(1)).addOrUpdate(
                new IndexEntry(entry.getElement(), IndexEntry.State.STORED));
        verify(index, times(1)).get(uid);
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
