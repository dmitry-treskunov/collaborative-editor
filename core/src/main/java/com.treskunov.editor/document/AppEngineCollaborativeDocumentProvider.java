package com.treskunov.editor.document;

import com.treskunov.editor.CollaborativeDocument;
import com.treskunov.editor.CollaborativeDocumentProvider;
import com.treskunov.editor.Collaborator;
import com.treskunov.editor.Operation;

import java.util.List;

/**
 * This is a sketch.
 */
public class AppEngineCollaborativeDocumentProvider implements CollaborativeDocumentProvider {

    @Override
    public CollaborativeDocument getById(String documentId) {
        //fetch document from db and return it
        return null;
    }

    @Override
    public List<CollaborativeDocument> getAllDocuments() {
        //fetch all documents from db and return them
        return null;
    }

    @Override
    public CollaborativeDocument create(String title) {
        //create document and wrap it with CollaborativeDocumentProxy
        return null;
    }

    private static class CollaborativeDocumentProxy implements CollaborativeDocument {

        private final CollaborativeDocument target;

        private CollaborativeDocumentProxy(CollaborativeDocument target) {
            this.target = target;
        }

        @Override
        public void registerCollaborator(Collaborator collaborator) {
            //save collaborator in db
            target.registerCollaborator(collaborator);
        }

        @Override
        public int getVersion() {
            return target.getVersion();
        }

        @Override
        public void apply(Operation operation) {
            //save operation in db
            target.apply(operation);
        }

        @Override
        public String asText() {
            return target.asText();
        }

        @Override
        public String getTitle() {
            return target.getTitle();
        }

        @Override
        public String getId() {
            return target.getId();
        }
    }
}
