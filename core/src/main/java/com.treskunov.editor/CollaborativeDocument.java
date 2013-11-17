package com.treskunov.editor;

/**
 * Document that can be edited by multiple collaborators
 * in the same time.
 */
public interface CollaborativeDocument extends Document {

    /**
     * Register {@link Collaborator} that is supposed to edit the document.
     */
    void registerCollaborator(Collaborator collaborator);

    /**
     * Get version of document.
     * Note that if you want to obtain document text and document version then
     * probably you should use {@link #getSnapshot()} method, that returns those values atomically.
     */
    int getVersion();

    /**
     * Provides ability to obtains consistent document content, version and another attributes.
     */
    DocumentSnapshot getSnapshot();
}
