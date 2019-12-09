package com.miquelmillan.context.domain.index;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class IndexEntry<I extends Serializable> {
    private final UUID id;
    private final I element;

    public IndexEntry(UUID id, I element) {
        this.id = id;
        this.element = element;
    }

    public UUID getId() {
        return id;
    }

    public I getElement() {
        return element;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexEntry<?> that = (IndexEntry<?>) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(element, that.element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, element);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IndexEntry{");
        sb.append("id=").append(id);
        sb.append(", element=").append(element);
        sb.append('}');
        return sb.toString();
    }
}
