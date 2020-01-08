package com.miquelmillan.backupr.uc.port.folder;

import com.miquelmillan.backupr.domain.contents.Contents;
import com.miquelmillan.backupr.domain.index.IndexEntry;
import com.miquelmillan.backupr.domain.index.IndexEntryRepository;
import com.miquelmillan.backupr.domain.location.Location;
import com.miquelmillan.backupr.uc.port.Resource;
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
@Qualifier("indexFolderUC")
public class IndexFolder {
    @Autowired
    private IndexEntryRepository index;

    private static Logger LOG = LoggerFactory.getLogger(IndexFolder.class);

    public IndexFolder(IndexEntryRepository index) {
        this.index = index;
    }

    public void indexFolder(File folder) throws IOException {
        Collection<File> files = FileUtils.listFiles(folder, null, true);
        List<IndexEntry> entries = new ArrayList();

        for (File file: files) {
            entries.add(new IndexEntry(
                            new Resource(UUID.nameUUIDFromBytes(file.getPath().getBytes()),
                                    file.getName(),
                                    new Location(file.getPath()),
                                    new Contents(file.getPath()))
                    )
            );
        }

        this.index.addOrUpdate(entries);
    }
}
