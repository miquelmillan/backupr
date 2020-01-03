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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class ResourceComponentTest {
    @Mock
    ResourceProcessor processor;

    @Mock
    ResourceRequester requester;

    @Mock
    IndexEntryRepository<Resource> index;

    String path;
    Location location;
    Resource sample;
    UUID uid;
    IndexEntry<Resource> entry;
    Resource item;

    @Before
    public void setup() throws FileNotFoundException {
        MockitoAnnotations.initMocks(this);
        this.path = ResourceRepository.class.getClassLoader().getResource("filesystem").getPath();
        this.location = new Location(path);
        this.uid = UUID.randomUUID();
        this.sample = new Resource(
                this.uid,
                "file1.txt",
                new Location(path + File.separatorChar + "file1.txt"),
                new Contents(path + File.separatorChar + "file1.txt"));
        this.entry = this.prepareIndexResultUnique();
        this.item = this.entry.getElement();
    }
    @Test
    public void resourceComponent_processUid_processOk() throws IOException, ResourceRepositoryException, ResourceUnknownException, ResourceUnavailableException {
        when(requester.requestOutputResource(sample.getLocation().getLocation())).thenReturn(this.prepareResourceResultUnique());
        when(processor.processOutputResource(sample)).thenReturn(this.prepareResourceResultUnique());
        when(index.get(uid)).thenReturn(entry);
        doNothing().when(index).addOrUpdate(entry);
        doNothing().when(index).addOrUpdate(new IndexEntry(entry.getElement(), IndexEntry.State.INDEXED));


        ResourceComponent component = new ResourceComponent(requester, processor, index);
        component.outboundResource(uid);

        verify(requester, times(1)).requestOutputResource(sample.getLocation().getLocation());
        verify(processor, times(1)).processOutputResource(sample);
        verify(index, times(1)).addOrUpdate(entry);
        verify(index, times(1)).addOrUpdate(
                new IndexEntry(entry.getElement(), IndexEntry.State.INDEXED));
        verify(index, times(1)).get(uid);
    }

    @Test
    public void resourceComponent_restoreUid_restoreOk() throws IOException, ResourceRepositoryException, ResourceUnknownException, ResourceUnavailableException {
        when(requester.requestInputResource(sample.getLocation().getLocation())).thenReturn(this.prepareResourceResultUnique());
        when(processor.processInputResource(sample)).thenReturn(this.prepareResourceResultUnique());
        when(index.get(uid)).thenReturn(entry);
        doNothing().when(index).addOrUpdate(entry);
        doNothing().when(index).addOrUpdate(new IndexEntry(entry.getElement(), IndexEntry.State.INDEXED));


        ResourceComponent component = new ResourceComponent(requester, processor, index);
        component.inboundResource(uid);

        verify(requester, times(1)).requestInputResource(sample.getLocation().getLocation());
        verify(processor, times(1)).processInputResource(sample);
        verify(index, times(1)).addOrUpdate(entry);
        verify(index, times(1)).addOrUpdate(
                new IndexEntry(entry.getElement(), IndexEntry.State.INDEXED));
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
        when(index.listAll()).thenReturn(Arrays.asList(new IndexEntry[] {prepareIndexResultUnique()}));

        ResourceComponent component = new ResourceComponent(requester, processor, index);
        component.listLocation();

        verify(index, times(1)).listAll();
    }

    @Test
    public void resourceComponent_outboundLocation_outboundOk() throws IOException,
                                                                ResourceRepositoryException,
                                                                ResourceUnknownException,
                                                                ResourceUnavailableException {
        // Mock component outboundUid to return always ok
        // Prepare an index of 5 elements
        // Check all of them are processed correctly
        when(index.get(any(Resource.class))).thenReturn(this.prepareListIndexResult());

        ResourceComponent component = new ResourceComponent(requester, processor, index);
        ResourceComponent testedComponent = Mockito.spy(component);
        Mockito.doNothing().when(testedComponent).outboundResource(any(UUID.class));

        assertTrue(testedComponent.outboundLocation(this.location));

        // Mock component outboundUid to return always failure
        // Prepare an index of 5 elements
        // Check all of them are processed incorrectly
        when(index.get(any(Resource.class))).thenReturn(this.prepareListIndexResult());
        Mockito.doThrow(ResourceUnavailableException.class).when(testedComponent).outboundResource(any(UUID.class));

        assertFalse(testedComponent.outboundLocation(this.location));

        verify(testedComponent, times(10)).outboundResource(any(UUID.class));
    }

    @Test
    public void resourceComponent_inboundLocation_inboundOk() throws IOException,
            ResourceRepositoryException,
            ResourceUnknownException,
            ResourceUnavailableException {
        // Mock component outboundUid to return always ok
        // Prepare an index of 5 elements
        // Check all of them are processed correctly
        when(index.get(any(Resource.class))).thenReturn(this.prepareListIndexResult());

        ResourceComponent component = new ResourceComponent(requester, processor, index);
        ResourceComponent testedComponent = Mockito.spy(component);
        Mockito.doNothing().when(testedComponent).inboundResource(any(UUID.class));

        assertTrue(testedComponent.inboundLocation(this.location));

        // Mock component outboundUid to return always failure
        // Prepare an index of 5 elements
        // Check all of them are processed incorrectly
        when(index.get(any(Resource.class))).thenReturn(this.prepareListIndexResult());
        Mockito.doThrow(ResourceUnavailableException.class).when(testedComponent).inboundResource(any(UUID.class));

        assertFalse(testedComponent.inboundLocation(this.location));

        verify(testedComponent, times(10)).inboundResource(any(UUID.class));
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
