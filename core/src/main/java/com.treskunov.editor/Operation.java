package com.treskunov.editor;

public interface Operation {

    void applyTo(DocumentContent document);

    /**
     * TODO Probably, this method should return {@link Collaborator} instance
     */
    String getInitiator();

    /**
     * Get version of the document the operation is supposed to be applied to.
     */
    int getDocumentVersion();

    Action getAction();
}
