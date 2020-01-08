package com.miquelmillan.backupr.uc.port.file;

import com.miquelmillan.backupr.domain.index.IndexEntry;
import com.miquelmillan.backupr.domain.index.IndexEntryRepository;
import com.miquelmillan.backupr.uc.port.Resource;
import com.miquelmillan.backupr.uc.port.exception.ResourceRepositoryException;
import com.miquelmillan.backupr.uc.port.exception.ResourceUnavailableException;
import com.miquelmillan.backupr.uc.port.exception.ResourceUnknownException;
import com.miquelmillan.backupr.uc.port.port.ResourceProcessor;
import com.miquelmillan.backupr.uc.port.port.ResourceRequester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Qualifier("backupFileUC")
public class BackupFile {
        @Autowired
        private ResourceRequester requester;

        @Autowired
        private ResourceProcessor processor;

        @Autowired
        private IndexEntryRepository index;

        private static Logger LOG = LoggerFactory.getLogger(BackupFile.class);

        public BackupFile(ResourceRequester requester,
                          ResourceProcessor processor,
                          IndexEntryRepository index) {
            this.requester = requester;
            this.processor = processor;
            this.index = index;
        }

    public void backupFile(UUID uid) throws ResourceUnknownException, ResourceUnavailableException, IOException, ResourceRepositoryException {
        Resource resource = (Resource) this.index.get(uid).getElement();
        if (resource == null) {
            throw new ResourceUnknownException("Resource UUID is unknown, could not process it");
        }
        // Update index state
        this.index.addOrUpdate(new IndexEntry(resource, IndexEntry.State.PENDING));

        // Process the outbound of the resource
        Resource result = this.requester.requestInputResource(resource);

        if (result != null) {
            this.processor.processInputResource(result);
        } else {
            throw new ResourceUnavailableException("Resource was not available locally");
        }

        // Update index state
        this.index.addOrUpdate(new IndexEntry(resource, IndexEntry.State.STORED));
    }
}
