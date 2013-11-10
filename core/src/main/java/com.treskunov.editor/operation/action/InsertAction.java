package com.treskunov.editor.operation.action;

import com.treskunov.editor.Action;
import com.treskunov.editor.ActionVisitor;
import com.treskunov.editor.DocumentContent;

import java.util.Objects;

/**
 * Represents action inserting a text in a document by index.
 */
public final class InsertAction implements Action {

    private String valueToInsert;
    private int index;

    /**
     * Use {@link InsertActionBuilder} to build action.
     */
    private InsertAction(String valueToInsert, int index) {
        this.valueToInsert = valueToInsert;
        this.index = index;
    }

    @Override
    public void applyTo(DocumentContent content) {
        content.insert(index, valueToInsert);
    }

    @Override
    public void accept(ActionVisitor visitor) {
        visitor.visit(this);
    }

    public String getValueToInsert() {
        return valueToInsert;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valueToInsert, index);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final InsertAction other = (InsertAction) obj;
        return Objects.equals(this.valueToInsert, other.valueToInsert) && Objects.equals(this.index, other.index);
    }

    @Override
    public String toString() {
        return "Insert '" + valueToInsert + "' to index '" + index + "'";
    }

    public static final class InsertActionBuilder {
        private String valueToInsert;

        private InsertActionBuilder(String valueToInsert) {
            this.valueToInsert = valueToInsert;
        }

        public static InsertActionBuilder insert(String valueToInsert) {
            return new InsertActionBuilder(valueToInsert);
        }

        public InsertAction toPosition(int index) {
            return new InsertAction(valueToInsert, index);
        }
    }
}