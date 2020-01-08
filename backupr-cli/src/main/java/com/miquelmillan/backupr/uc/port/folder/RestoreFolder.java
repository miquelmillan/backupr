package com.miquelmillan.backupr.uc.port.folder;

import com.miquelmillan.backupr.domain.index.IndexEntryRepository;
import com.miquelmillan.backupr.domain.location.Location;
import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.domain.resource.exception.ResourceRepositoryException;
import com.miquelmillan.backupr.domain.resource.exception.ResourceUnavailableException;
import com.miquelmillan.backupr.domain.resource.exception.ResourceUnknownException;
import com.miquelmillan.backupr.uc.port.port.ResourceComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@Qualifier("restoreFolderUC")
public class RestoreFolder {
    @Autowired
    private ResourceComponent component;
    @Autowired
    private IndexEntryRepository index;

    private static Logger LOG = LoggerFactory.getLogger(RestoreFolder.class);

    public RestoreFolder(ResourceComponent component,
                        IndexEntryRepository index) {
        this.component = component;
        this.index = index;
    }

    public boolean restoreFolder(File folder) {
        Location loc = new Location(folder.getPath());
        List<Resource> resources = this.index.get(new Resource(loc));
        LOG.info("inbounding resources. Size: {}", resources.size());

        int outboundedCount = resources.parallelStream()
                .map( r -> {
                    try {
                        LOG.debug("inbounding resource: {}", r.getId());
                        this.component.inboundResource(r.getId());
                        LOG.info("resource inbounded");

                        return 1;
                    } catch (   IOException | ResourceUnknownException |
                            ResourceUnavailableException | ResourceRepositoryException e) {
                        LOG.error("An problem ocurred while inbounding resource", e);
                    }
                    return 0;
                })
                .reduce(0, Integer::sum);

        if (resources.size() != outboundedCount){
            return false;
        }

        return true;
    }
}
