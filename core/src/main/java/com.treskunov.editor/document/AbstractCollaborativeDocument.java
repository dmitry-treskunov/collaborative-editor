package com.treskunov.editor.document;

import com.treskunov.editor.CollaborativeDocument;
import com.treskunov.editor.Collaborator;
import com.treskunov.editor.Operation;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AbstractCollaborativeDocument implements CollaborativeDocument {

    /**
     * Number of reads is much more that number writes.
     */
    private List<Collaborator> collaborators = new CopyOnWriteArrayList<>();

    public void registerCollaborator(Collaborator collaborator) {
        collaborators.add(collaborator);
    }

    protected void notifyCollaborators(Operation operationToApply) {
        for (Collaborator collaborator : collaborators) {
            collaborator.onOperationApplied(operationToApply);
        }
    }
}