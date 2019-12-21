package com.miquelmillan.context.domain.resource;

import com.miquelmillan.context.domain.contents.Contents;
import com.miquelmillan.context.domain.index.IndexEntry;
import com.miquelmillan.context.domain.index.IndexEntryRepository;
import com.miquelmillan.context.domain.location.Location;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Qualifier("resourceComponent")
public class ResourceComponent {
    @Autowired
    private ResourceRequester requester;
    @Autowired
    private ResourceProcessor processor;
    @Autowired
    private IndexEntryRepository index;


    /**
     * TODO
     * inbound/outbound File
     * inbound/outbound Location (Folder)
     * list indexed files (status, origin, destination, and UUID)
     * Daemon setup ==> NOT in the component
     *
     */
    public ResourceComponent(ResourceRequester requester,
                             ResourceProcessor processor,
                             IndexEntryRepository index) {
        this.requester = requester;
        this.processor = processor;
        this.index = index;
    }


    public void outboundResource(UUID uid) throws IOException,
                                                    ResourceUnknownException,
                                                    ResourceUnavailableException,
                                                    ResourceRepositoryException {
        Resource resource = (Resource) this.index.get(uid).getElement();
        if (resource == null) {
            throw new ResourceUnknownException("Resource UUID is unknown, could not process it");
        }
        // Update index state
        this.index.addOrUpdate(new IndexEntry(uid, resource, IndexEntry.State.PENDING));

        // Process the outbound of the resource
        ResourceResult result = this.requester.requestOutputResource(resource.getLocation().getLocation());

        switch (result.getResources().values().size()) {
            case 0:
                throw new ResourceUnavailableException("Resource was not available locally");
            case 1:
                this.processor.processOutputResource(result.getResources().values().iterator().next());
                break;
            default:
                throw new ResourceRepositoryException("UUID has more than 1 resources");

        }

        // Update index state
        this.index.addOrUpdate(new IndexEntry(uid, resource, IndexEntry.State.INDEXED));
    }


    public void inboundResource(UUID uid) throws IOException,
                                                    ResourceUnknownException,
                                                    ResourceUnavailableException,
                                                    ResourceRepositoryException {
        Resource resource = (Resource) this.index.get(uid).getElement();
        if (resource == null) {
            throw new ResourceUnknownException("Resource UUID is unknown, could not process it");
        }
        // Update index state
        this.index.addOrUpdate(new IndexEntry(uid, resource, IndexEntry.State.PENDING));

        // Process the outbound of the resource
        ResourceResult result = this.requester.requestInputResource(resource.getLocation().getLocation());

        switch (result.getResources().values().size()) {
            case 0:
                throw new ResourceUnavailableException("Resource was not available locally");
            case 1:
                this.processor.processInputResource(result.getResources().values().iterator().next());
                break;
            default:
                throw new ResourceRepositoryException("UUID has more than 1 resources");

        }

        // Update index state
        this.index.addOrUpdate(new IndexEntry(uid, resource, IndexEntry.State.INDEXED));
    }

    public List<IndexEntry> listLocation() {
        return new ArrayList(this.index.listAll());
    }

    public void indexLocation(Location location) throws IOException {
        Collection<File> files = FileUtils.listFiles(new File(location.getLocation()), null, true);
        List<IndexEntry> entries = new ArrayList();

        for (File file: files) {
            entries.add(new IndexEntry(UUID.randomUUID(),
                            new Resource(   file.getName(),
                                    new Location(file.getPath()),
                                    new Contents(file.getPath()))
                    )
            );
        }

        this.index.addOrUpdate(entries);
    }


    public ResourceResult outboundLocation(Location location) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private ResourceResult inboundLocation(Location location) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
