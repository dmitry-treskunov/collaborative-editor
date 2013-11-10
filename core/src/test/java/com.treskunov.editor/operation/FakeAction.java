package com.treskunov.editor.operation;

import com.treskunov.editor.Action;
import com.treskunov.editor.ActionVisitor;
import com.treskunov.editor.DocumentContent;

import java.util.Objects;

public class FakeAction implements Action{

    private String id;

    public FakeAction(String id) {
        this.id = id;
    }

    @Override
    public void applyTo(DocumentContent content) {
        //do nothing
    }

    @Override
    public void accept(ActionVisitor visitor) {
        //do nothing
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final FakeAction other = (FakeAction) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "FakeAction{" +
                "id='" + id + '\'' +
                '}';
    }
}
