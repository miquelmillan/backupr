package com.miquelmillan.context.domain.index;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class IndexEntry<I extends Serializable> {
    public enum State {
        PENDING("pending"),
        INDEXED("indexed"),
        DELETED("deleted");

        private String state;

        State(String s){
            this.state = s;
        }
    }

    private final UUID id;
    private final I element;
    private final State state;

    public IndexEntry(UUID id, I element) {
        this.id = id;
        this.element = element;
        this.state = State.PENDING;
    }

    public IndexEntry(UUID id, I element, State state) {
        this.id = id;
        this.element = element;
        this.state = state;
    }

    public UUID getId() {
        return id;
    }

    public I getElement() {
        return element;
    }

    public State getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexEntry<?> that = (IndexEntry<?>) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(element, that.element) &&
                state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, element, state);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IndexEntry {");
        sb.append("id=").append(id);
        sb.append(", element=").append(element);
        sb.append(", state=").append(state);
        sb.append('}');
        return sb.toString();
    }
}
