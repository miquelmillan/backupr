package com.miquelmillan.context.domain.index;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IndexEntryRepository {
    void addOrUpdate(IndexEntry entry) throws IOException;
    void addOrUpdate(List<IndexEntry> entry) throws IOException;

    void remove(IndexEntry entry) throws IOException;
    void remove(List<IndexEntry> entry) throws IOException;

    IndexEntry get(UUID id);

    Collection<IndexEntry> listAll();
}
