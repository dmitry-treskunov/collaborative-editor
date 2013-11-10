package com.treskunov.editor;

public interface CollaborativeDocumentRepository {

    /**
     * This repository provides collection-like interface so
     * it returns null if document with such id was not found.
     */
    CollaborativeDocument getById(String documentId);

    /**
     * Returns saved version of document.
     * <p/>
     * After saving returned instance should be used by clients.
     * Note that this instance can be not the same as document passed in parameters.
     */
    CollaborativeDocument save(String documentId, CollaborativeDocument document);
}
