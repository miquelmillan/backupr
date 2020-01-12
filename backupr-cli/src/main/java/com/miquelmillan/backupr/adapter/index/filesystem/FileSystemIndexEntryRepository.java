package com.miquelmillan.backupr.adapter.index.filesystem;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.miquelmillan.backupr.domain.index.IndexEntry;
import com.miquelmillan.backupr.uc.port.repository.IndexEntryRepository;
import com.miquelmillan.backupr.domain.resource.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        entries = new HashMap<>();
        this.loadIndex();
    }

    @Override
    public void addOrUpdate(IndexEntry<Resource> entry) throws IOException{
        entries.put(entry.getElement().getId(), entry);
        this.syncIndex();
    }

    @Override
    public void addOrUpdate(List<IndexEntry<Resource>> newEntries) throws IOException {
        Map<UUID, IndexEntry<Resource>> newIndex = new HashMap<>();
        Map<UUID, IndexEntry<Resource>> itemsMap = new HashMap<>();

        // Put list of new entries in a map
        newEntries.forEach(
                elem -> {
                    itemsMap.put(elem.getElement().getId(), elem);
                    newIndex.put(elem.getElement().getId(), elem);
                }
        );

        // Check list of items
        entries.keySet().forEach(
                key -> {
                    if (!itemsMap.containsKey(key)) {
                        // If item is not provided, delete it
                        IndexEntry<Resource> entry = entries.get(key);
                        entry.setState(IndexEntry.State.DELETED);
                        newIndex.put(key, entry);
                    }
                }
        );

        // replace entries map
        this.entries = newIndex;
        this.syncIndex();
    }

    @Override
    public void remove(IndexEntry<Resource> entry) throws IOException{
        entries.remove(entry.getElement().getId());
        this.syncIndex();
    }

    @Override
    public IndexEntry get(UUID id) {
        return entries.get(id);
    }

    @Override
    public List<Resource> get(Resource params) {
        List<Resource> result = null;

        if (params.getLocation() != null){
            String loc = params.getLocation().getLocation();
            result =
                    entries.values().
                            stream().
                            filter( r -> {
                                if (r.getElement().getLocation() != null) {
                                    return r.getElement().getLocation().getLocation().contains(loc);
                                }
                                return false;
                            }).map( r -> r.getElement())
                            .collect(Collectors.toList());

        }

        return result;
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
