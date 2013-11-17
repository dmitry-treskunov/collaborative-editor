package com.treskunov.editor.document;

import com.treskunov.editor.CollaborativeDocument;
import com.treskunov.editor.CollaborativeDocumentRepository;
import com.treskunov.editor.Collaborator;
import com.treskunov.editor.Operation;

/**
 * This is a sketch.
 */
public class AppEngineCollaborativeDocumentRepository implements CollaborativeDocumentRepository {

    @Override
    public CollaborativeDocument getById(String documentId) {
        //fetch document from db and return it
        return null;
    }

    @Override
    public CollaborativeDocument save(String documentId, CollaborativeDocument document) {
        CollaborativeDocumentProxy proxy = new CollaborativeDocumentProxy(document);
        //save proxy to db
        return proxy;
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
