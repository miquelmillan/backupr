package com.miquelmillan.backupr.uc.local;

import com.miquelmillan.backupr.uc.UseCase;
import com.miquelmillan.backupr.uc.port.repository.IndexEntryRepository;
import com.miquelmillan.backupr.domain.location.Location;
import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.domain.resource.exception.ResourceRepositoryException;
import com.miquelmillan.backupr.domain.resource.exception.ResourceUnavailableException;
import com.miquelmillan.backupr.domain.resource.exception.ResourceUnknownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
@Qualifier("backupFolderUC")
public class BackupFolder implements UseCase {

    @Autowired
    private BackupFile backupFile;
    @Autowired
    private IndexEntryRepository index;

    private static Logger LOG = LoggerFactory.getLogger(BackupFolder.class);


    public BackupFolder(BackupFile backupFile,
                             IndexEntryRepository index) {
        this.backupFile = backupFile;
        this.index = index;
    }

    public boolean backupFolder(File folder) {
        Location loc = new Location(folder.getPath());
        List<Resource> resources = this.index.get(new Resource(loc));
        LOG.info("outbounding resources. Size: {}", resources.size());
        int outboundedCount = resources.parallelStream()
                .map( r -> {
                    try {
                        LOG.info("outbounding resource: {}", r.toString());
                        this.backupFile.backupFile(r.getId());
                        LOG.info("resource outbounded");
                        return 1;
                    } catch (   IOException | ResourceUnknownException |
                            ResourceUnavailableException | ResourceRepositoryException e) {
                        LOG.error("An problem ocurred while outbounding resource", e);
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
