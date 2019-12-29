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

    private I element;
    private State state;

    public IndexEntry() {
    }

    public IndexEntry(I element) {
        this.element = element;
        this.state = State.PENDING;
    }

    public IndexEntry(I element, State state) {
        this.element = element;
        this.state = state;
    }

    public I getElement() {
        return element;
    }

    public void setElement(I element) {
        this.element = element;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexEntry<?> that = (IndexEntry<?>) o;
        return element.equals(that.element) &&
                state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(element, state);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IndexEntry{");
        sb.append("element=").append(element);
        sb.append(", state=").append(state);
        sb.append('}');
        return sb.toString();
    }
}
