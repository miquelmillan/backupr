package com.miquelmillan.context.infrastructure.filesystem.index;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miquelmillan.context.domain.index.IndexEntry;
import com.miquelmillan.context.domain.index.IndexEntryRepository;
import com.miquelmillan.context.domain.resource.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Repository
@Qualifier("fsIndexEntryRepository")
public class FileSystemIndexEntryRepository implements IndexEntryRepository<Resource> {
    private String indexLocation;
    private Map<UUID, IndexEntry<Resource>> entries;
    private ObjectMapper mapper;

    public FileSystemIndexEntryRepository(@Value("${application.index.location}") String indexLocation) throws IOException {
        if (indexLocation == null){
            throw new IllegalArgumentException("backupr index location cannot be null");
        }
        this.indexLocation = indexLocation;
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        entries = new HashMap();
        this.loadIndex();
    }

    @Override
    public void addOrUpdate(IndexEntry<Resource> entry) throws IOException{
        entries.put(entry.getElement().getId(), entry);
        this.syncIndex();
    }

    @Override
    public void addOrUpdate(List<IndexEntry<Resource>> newEntries) throws IOException {
        newEntries.forEach(
                elem -> entries.put(elem.getElement().getId(), elem)
        );
        this.syncIndex();
    }

    @Override
    public void remove(IndexEntry<Resource> entry) throws IOException{
        entries.remove(entry.getElement().getId());
        this.syncIndex();
    }

    @Override
    public void remove(List<IndexEntry<Resource>> entriesToRemove) throws IOException {
        entriesToRemove.forEach(
                elem -> entries.remove(elem.getElement().getId())
        );
        this.syncIndex();
    }

    @Override
    public IndexEntry get(UUID id) {
        return entries.get(id);
    }

    public Collection<IndexEntry<Resource>> listAll() {
        return this.entries.values();
    }

    private void loadIndex() throws IOException {
        File index = new File(this.indexLocation);

        if (index.exists()) {
            if (index.length() > 0) {
                entries = mapper.readValue(index, new TypeReference<Map<UUID, IndexEntry<Resource>>>(){ });
            }
        } else {
            index.getParentFile().mkdirs();
            index.createNewFile();
            entries = new HashMap();
        }
    }

    private void syncIndex() throws IOException {
        mapper.writeValue(new File(indexLocation), entries);
    }
}
