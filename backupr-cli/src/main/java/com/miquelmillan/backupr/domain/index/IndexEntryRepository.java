package com.miquelmillan.backupr.domain.index;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface IndexEntryRepository<T extends Serializable> {
    void addOrUpdate(IndexEntry<T> entry) throws IOException;
    void addOrUpdate(List<IndexEntry<T>> entry) throws IOException;

    void remove(IndexEntry<T> entry) throws IOException;

    IndexEntry<T> get(UUID id);
    List<T> get(T params);

    Collection<IndexEntry<T>> listAll();
}
