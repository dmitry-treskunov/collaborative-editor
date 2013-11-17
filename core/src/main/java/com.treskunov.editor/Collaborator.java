package com.treskunov.editor;

/**
 * Collaborator participated in {@link CollaborativeDocument} editing.
 * <p/>
 * Note that implementation of this interface should override {@link Object#equals(Object)}
 * method to properly work
 */
public interface Collaborator {

    /**
     * @see com.treskunov.editor.Document#getId()
     */
    String getDocumentId();

    /**
     * Callback called when operation is applied on the observed document.
     */
    void onOperationApplied(Operation operation);
}
