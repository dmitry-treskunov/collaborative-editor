package com.treskunov.editor.operation.action;

import com.treskunov.editor.Action;
import com.treskunov.editor.ActionVisitor;
import com.treskunov.editor.DocumentContent;

import java.util.Objects;

/**
 * Represents action of deleting sequence of characters from a document
 * starting from specified index.
 */
public class DeleteAction implements Action {
    private final int startIndex;
    private final String valueToDelete;

    /**
     * Use {@link DeleteActionBuilder} to build action.
     */
    private DeleteAction(int startIndex, String valueToDelete) {
        this.startIndex = startIndex;
        this.valueToDelete = valueToDelete;
    }

    public int getIndex() {
        return startIndex;
    }

    public String getValueToDelete() {
        return valueToDelete;
    }

    @Override
    public void applyTo(DocumentContent content) {
        content.delete(startIndex, valueToDelete.length());
    }

    @Override
    public void accept(ActionVisitor visitor) {
        visitor.visit(this);
    }


    @Override
    public int hashCode() {
        return Objects.hash(valueToDelete, startIndex);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DeleteAction other = (DeleteAction) obj;
        return Objects.equals(this.valueToDelete, other.valueToDelete) && Objects.equals(this.startIndex, other.startIndex);
    }

    @Override
    public String toString() {
        return "Delete '" + valueToDelete + "' from index '" + startIndex + "'";
    }

    public static final class DeleteActionBuilder {
        private String valueToDelete;

        private DeleteActionBuilder(String valueToDelete) {
            this.valueToDelete = valueToDelete;
        }

        public static DeleteActionBuilder delete(String valueToDelete) {
            return new DeleteActionBuilder(valueToDelete);
        }

        public DeleteAction startFrom(int index) {
            return new DeleteAction(index, valueToDelete);
        }
    }
}
