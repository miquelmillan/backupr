package com.miquelmillan.context.domain.resource;

import com.miquelmillan.context.domain.contents.Contents;
import com.miquelmillan.context.domain.index.IndexEntry;
import com.miquelmillan.context.domain.index.IndexEntryRepository;
import com.miquelmillan.context.domain.location.Location;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
@Qualifier("resourceComponent")
public class ResourceComponent {
    @Autowired
    private ResourceRequester requester;
    @Autowired
    private ResourceProcessor processor;
    @Autowired
    private IndexEntryRepository index;

    private static Logger LOG = LoggerFactory.getLogger(ResourceComponent.class);
    /**
     * TODO
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
        this.index.addOrUpdate(new IndexEntry(resource, IndexEntry.State.PENDING));

        // Process the outbound of the resource
        Resource result = this.requester.requestOutputResource(resource.getLocation().getLocation());

        if (result != null) {
            this.processor.processOutputResource(result);
        } else {
            throw new ResourceUnavailableException("Resource was not available locally");
        }

        // Update index state
        this.index.addOrUpdate(new IndexEntry(resource, IndexEntry.State.INDEXED));
    }

    public boolean outboundLocation(Location loc) {

        List<Resource> resources = this.index.get(new Resource(loc));

        int outboundedCount = resources.parallelStream()
                .map( r -> {
                    try {
                        LOG.info("outbounding resource: {}", r.toString());
                        this.outboundResource(r.getId());
                        LOG.info("resource outbounded");
                        return 1;
                    } catch (   IOException | ResourceUnknownException |
                                ResourceUnavailableException | ResourceRepositoryException e) {
                        e.printStackTrace();
                    }
                    return 0;
                })
                .reduce(0, Integer::sum);

        if (resources.size() != outboundedCount){
            return false;
        }

        return true;
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
        this.index.addOrUpdate(new IndexEntry(resource, IndexEntry.State.PENDING));

        // Process the outbound of the resource
        Resource result = this.requester.requestInputResource(resource.getLocation().getLocation());

        if (result != null) {
            this.processor.processInputResource(result);
        } else {
            throw new ResourceUnavailableException("Resource was not available locally");
        }


        // Update index state
        this.index.addOrUpdate(new IndexEntry(resource, IndexEntry.State.INDEXED));
    }

    public boolean inboundLocation(Location loc) {
        List<Resource> resources = this.index.get(new Resource(loc));

        int outboundedCount = resources.parallelStream()
                .map( r -> {
                    try {
                        this.inboundResource(r.getId());
                        return 1;
                    } catch (   IOException | ResourceUnknownException |
                            ResourceUnavailableException | ResourceRepositoryException e) {
                        e.printStackTrace();
                    }
                    return 0;
                })
                .reduce(0, Integer::sum);

        if (resources.size() != outboundedCount){
            return false;
        }

        return true;
    }


    public List<IndexEntry<Resource>> listLocation() {
        return new ArrayList(this.index.listAll());
    }

    public void indexLocation(Location location) throws IOException {
        Collection<File> files = FileUtils.listFiles(new File(location.getLocation()), null, true);
        List<IndexEntry> entries = new ArrayList();

        for (File file: files) {
            entries.add(new IndexEntry(
                            new Resource(UUID.randomUUID(),
                                    file.getName(),
                                    new Location(file.getPath()),
                                    new Contents(file.getPath()))
                    )
            );
        }

        this.index.addOrUpdate(entries);
    }
}
