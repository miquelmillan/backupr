package com.miquelmillan.backupr.uc.local;

import com.miquelmillan.backupr.domain.index.IndexEntry;
import com.miquelmillan.backupr.uc.UseCase;
import com.miquelmillan.backupr.uc.port.repository.IndexEntryRepository;
import com.miquelmillan.backupr.domain.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier("listIndexedFolderUC")
public class ListIndexedFolder implements UseCase {
    @Autowired
    private IndexEntryRepository index;

    private static Logger LOG = LoggerFactory.getLogger(BackupFolder.class);

    public ListIndexedFolder(IndexEntryRepository index) {
        this.index = index;
    }

    public List<IndexEntry<Resource>>  listFolder() throws IOException {
        return new ArrayList(this.index.listAll());
    }
}
