package com.miquelmillan.context.domain.resource;

import com.miquelmillan.context.domain.contents.Contents;
import com.miquelmillan.context.domain.index.IndexEntry;
import com.miquelmillan.context.domain.index.IndexEntryRepository;
import com.miquelmillan.context.domain.location.Location;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ResourceComponentTest {
    @Mock
    ResourceProcessor processor;

    @Mock
    ResourceRequester requester;

    @Mock
    IndexEntryRepository index;


    String path;
    Location location;
    Resource sample;
    UUID uid;

    @Before
    public void setup() throws FileNotFoundException {
        MockitoAnnotations.initMocks(this);
        this.path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();
        this.location = new Location(path);
        this.sample = new Resource("file1.txt",
                new Location(path + File.separatorChar + "file1.txt"),
                new Contents(path + File.separatorChar + "file1.txt"));
        this.uid = UUID.randomUUID();
    }
    @Test
    public void resourceComponent_processUid_processOk() throws IOException, ResourceRepositoryException, ResourceUnknownException, ResourceUnavailableException {
        IndexEntry entry = this.prepareIndexResult();

        when(requester.requestOutputResource(sample.getLocation().getLocation())).thenReturn(this.prepareResourceResult());
        when(processor.processOutputResource(sample)).thenReturn(this.prepareResourceResult());
        when(index.get(uid)).thenReturn(entry);
        doNothing().when(index).addOrUpdate(entry);
        doNothing().when(index).addOrUpdate(new IndexEntry(entry.getId(), entry.getElement(), IndexEntry.State.INDEXED));


        ResourceComponent component = new ResourceComponent(requester, processor, index);
        component.outboundResource(uid);

        verify(requester, times(1)).requestOutputResource(sample.getLocation().getLocation());
        verify(processor, times(1)).processOutputResource(sample);
        verify(index, times(1)).addOrUpdate(entry);
        verify(index, times(1)).addOrUpdate(
                new IndexEntry(entry.getId(), entry.getElement(), IndexEntry.State.INDEXED));
        verify(index, times(1)).get(uid);
    }

    @Test
    public void resourceComponent_restoreUid_restoreOk() throws IOException, ResourceRepositoryException, ResourceUnknownException, ResourceUnavailableException {
        IndexEntry entry = this.prepareIndexResult();

        when(requester.requestInputResource(sample.getLocation().getLocation())).thenReturn(this.prepareResourceResult());
        when(processor.processInputResource(sample)).thenReturn(this.prepareResourceResult());
        when(index.get(uid)).thenReturn(entry);
        doNothing().when(index).addOrUpdate(entry);
        doNothing().when(index).addOrUpdate(new IndexEntry(entry.getId(), entry.getElement(), IndexEntry.State.INDEXED));


        ResourceComponent component = new ResourceComponent(requester, processor, index);
        component.inboundResource(uid);

        verify(requester, times(1)).requestInputResource(sample.getLocation().getLocation());
        verify(processor, times(1)).processInputResource(sample);
        verify(index, times(1)).addOrUpdate(entry);
        verify(index, times(1)).addOrUpdate(
                new IndexEntry(entry.getId(), entry.getElement(), IndexEntry.State.INDEXED));
        verify(index, times(1)).get(uid);

    }

    @Test
    public void resourceComponent_indexLocation_indexOk() throws IOException {
        doNothing().when(index).addOrUpdate(any(List.class));

        ResourceComponent component = new ResourceComponent(requester, processor, index);
        component.indexLocation(this.location);

        verify(index, times(1)).addOrUpdate(any(List.class));
    }

    @Test
    public void resourceComponent_listLocation_listOk() throws FileNotFoundException {
        when(index.listAll()).thenReturn(Arrays.asList(new IndexEntry[] {prepareIndexResult()}));

        ResourceComponent component = new ResourceComponent(requester, processor, index);
        component.listLocation();

        verify(index, times(1)).listAll();
    }


    private IndexEntry<Resource> prepareIndexResult() throws FileNotFoundException {
        Resource r = new Resource("file1.txt",
                new Location(path + File.separatorChar + "file1.txt"),
                new Contents(path + File.separatorChar + "file1.txt" ));

        return new IndexEntry<>(uid, r);
    }

    private ResourceResult prepareResourceResult() throws FileNotFoundException {
        ResourceResult result = new ResourceResult();
        Map<String, Resource> resources = new HashMap();

        resources.put("file1.txt",
                new Resource("file1.txt",
                        new Location(path + File.separatorChar + "file1.txt"),
                        new Contents(path + File.separatorChar + "file1.txt" )));

        result.setResources(resources);
        return result;
    }
}
