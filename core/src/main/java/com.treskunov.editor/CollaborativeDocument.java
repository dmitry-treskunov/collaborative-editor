package com.treskunov.editor;

public interface CollaborativeDocument extends MutableDocument {

    void registerCollaborator(Collaborator collaborator);

    int getVersion();
}
