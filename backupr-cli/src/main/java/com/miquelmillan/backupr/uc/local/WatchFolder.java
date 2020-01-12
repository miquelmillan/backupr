package com.miquelmillan.backupr.uc.local;

import com.miquelmillan.backupr.adapter.observer.filesystem.FolderObserver;
import com.miquelmillan.backupr.domain.contents.Contents;
import com.miquelmillan.backupr.domain.index.IndexEntry;
import com.miquelmillan.backupr.domain.resource.Resource;
import com.miquelmillan.backupr.domain.resource.ResourceEvent;
import com.miquelmillan.backupr.domain.resource.exception.ResourceRepositoryException;
import com.miquelmillan.backupr.domain.resource.exception.ResourceUnavailableException;
import com.miquelmillan.backupr.domain.resource.exception.ResourceUnknownException;
import com.miquelmillan.backupr.uc.UseCase;
import com.miquelmillan.backupr.uc.port.repository.IndexEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class WatchFolder implements UseCase, Observer {

    private static Logger LOG = LoggerFactory.getLogger(WatchFolder.class);

    private FolderObserver watcher;

    private BackupFile backupFile;

    private IndexEntryRepository<Resource> index;

    public void watch(File file) throws IOException {
        if (file.exists() && file.isDirectory()){
            watcher = new FolderObserver(file.toPath());
            watcher.processEvents();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof FolderObserver) {
            if (arg instanceof ResourceEvent){
                ResourceEvent resourceEvt = (ResourceEvent) arg;

                LOG.debug("UC => new Event: {}", resourceEvt);

                switch (resourceEvt.getType()) {
                    case CREATED:
                    case UPDATED:
                        // Find item in index
                        Resource res = index.getUnique(resourceEvt.getResource());

                        if (res == null) {
                            try {
                                index.addOrUpdate(new IndexEntry(resourceEvt.getResource()));
                                res = index.getUnique(resourceEvt.getResource());
                            } catch (IOException e) {
                                LOG.error("An error has ocurred while indexing file: {}", res.getId());
                                LOG.error("Specific error: ", e);
                            }
                        }

                        // Needs to be a test, if previous operation fails and we have no UUID
                        if (res != null) {
                            // invoke Use Case for backupFile
                            try {
                                backupFile.backupFile(res.getId());
                            } catch (ResourceUnknownException | ResourceUnavailableException |
                                    IOException | ResourceRepositoryException e) {
                                LOG.error("An error has ocurred while processing file: {}", res.getId());
                                LOG.error("Specific error: ", e);
                            }
                        }
                        break;
                    case DELETED:
                        LOG.debug("resource deletion detected. Nothing to do yet");
                        break;
                }
            }
        }
    }


}
