package com.treskunov.editor;

public interface Operation {

    void applyTo(DocumentContent document);

    String getInitiator();

    /**
     * Get version of the document the operation is supposed to be applied to.
     */
    int getDocumentVersion();

    void accept(ActionVisitor visitor);

    Action getAction();
}
