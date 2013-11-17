package com.treskunov.editor.document;

import com.treskunov.editor.CollaborativeDocument;
import com.treskunov.editor.Collaborator;
import com.treskunov.editor.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * General logic for collaborator notifications.
 */
public abstract class AbstractCollaborativeDocument implements CollaborativeDocument {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCollaborativeDocument.class);

    /**
     * Number of reads is much more that number of writes.
     */
    private List<Collaborator> collaborators = new CopyOnWriteArrayList<>();

    public void registerCollaborator(Collaborator collaborator) {
        logger.info("Registering collaborator " + collaborator);
        collaborators.add(collaborator);
    }

    @Override
    public void unregisterCollaborator(Collaborator collaborator) {
        logger.info("Unregistering collaborator " + collaborator);
        collaborators.remove(collaborator);
    }

    protected void notifyCollaborators(Operation operationToApply) {
        logger.info("Broadcasting operation " + operationToApply);
        for (Collaborator collaborator : collaborators) {
            collaborator.onOperationApplied(operationToApply);
        }
    }
}