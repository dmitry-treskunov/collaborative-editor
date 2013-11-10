package com.treskunov.editor.document;

import com.google.inject.Singleton;
import com.treskunov.editor.CollaborativeDocument;
import com.treskunov.editor.CollaborativeDocumentRepository;
import com.treskunov.editor.DocumentContent;
import com.treskunov.editor.operation.OperationRebaser;
import net.jcip.annotations.ThreadSafe;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of {@link CollaborativeDocumentRepository} that stores document in
 * internal collection.
 * <p/>
 * This is not very useful for real life, but is suitable for prototype.
 *
 * @see AppEngineCollaborativeDocumentRepository
 */
@Singleton
@ThreadSafe
public class InMemoryCollaborativeDocumentRepository implements CollaborativeDocumentRepository {

    private Map<String, CollaborativeDocument> documents = new ConcurrentHashMap<>();

    /**
     * TODO get rid of document initialization here
     */
    @Inject
    public InMemoryCollaborativeDocumentRepository(OperationRebaser operationRebaser) {
        DocumentContent content = new StringDocumentContent();
        documents.put("document1", new SynchronizedDocument(content, "Hello!", operationRebaser));
    }

    @Override
    public CollaborativeDocument getById(String documentId) {
        return documents.get(documentId);
    }

    @Override
    public CollaborativeDocument save(String documentId, CollaborativeDocument document) {
        documents.put(documentId, document);
        return document;
    }
}
