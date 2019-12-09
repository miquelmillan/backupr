package com.miquelmillan.context.infrastructure.filesystem.resource;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miquelmillan.context.domain.index.IndexEntry;
import com.miquelmillan.context.domain.index.IndexEntryRepository;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileSystemIndexEntryRepository implements IndexEntryRepository {
    private String indexLocation;
    private Map<UUID, IndexEntry> entries;
    private ObjectMapper mapper;

    public FileSystemIndexEntryRepository(@Value("${application.index.location}") String indexLocation) throws IOException {
        if (indexLocation == null){
            throw new IllegalArgumentException("backupr index location cannot be null");
        }
        this.indexLocation = indexLocation;
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        this.loadIndex();
    }

    @Override
    public void addOrUpdate(IndexEntry entry) throws IOException{
        entries.put(entry.getId(), entry);
        this.syncIndex();
    }

    @Override
    public void addOrUpdate(List<IndexEntry> newEntries) throws IOException {
        newEntries.forEach(
                elem -> entries.put(elem.getId(), elem)
        );
        this.syncIndex();
    }

    @Override
    public void remove(IndexEntry entry) throws IOException{
        entries.remove(entry.getId());
        this.syncIndex();
    }

    @Override
    public void remove(List<IndexEntry> entriesToRemove) throws IOException {
        entriesToRemove.forEach(
                elem -> entries.remove(elem.getId())
        );
        this.syncIndex();
    }

    @Override
    public IndexEntry get(UUID id) {
        return entries.get(id);
    }

    public Collection<IndexEntry> listAll() {
        return this.entries.values();
    }

    private void loadIndex() throws IOException {
        File index = new File(this.indexLocation);
        if (index.exists()) {
            entries = mapper.readValue(index, entries.getClass());
        } else {
            index.createNewFile();
            entries = new HashMap();
        }
    }

    private void syncIndex() throws IOException {
        mapper.writeValue(new File(indexLocation), entries);
    }
}
