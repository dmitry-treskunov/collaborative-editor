package com.treskunov.editor;

public interface CollaborativeDocumentRepository {

    /**
     * This repository provides collection-like interface so
     * it returns null if document with such id was not found.
     */
    CollaborativeDocument getById(String documentId);

    /**
     * Get all existing documents or empty iterable if there is no documents yet.
     */
    Iterable<CollaborativeDocument> getAllDocuments();

    /**
     * Returns created document.
     */
    CollaborativeDocument create(String title);
}
