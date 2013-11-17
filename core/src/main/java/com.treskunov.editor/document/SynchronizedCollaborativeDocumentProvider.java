package com.treskunov.editor.document;

import com.google.inject.Singleton;
import com.treskunov.editor.CollaborativeDocument;
import com.treskunov.editor.CollaborativeDocumentProvider;
import com.treskunov.editor.DocumentContent;
import com.treskunov.editor.operation.OperationRebaser;
import net.jcip.annotations.ThreadSafe;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of {@link CollaborativeDocumentProvider}
 * that operates with {@link SynchronizedCollaborativeDocumentProvider}
 * and stores documents in the internal collection.
 * <p/>
 * This is not very useful for real life, but is suitable for prototype.
 *
 * @see AppEngineCollaborativeDocumentProvider
 */
@Singleton
@ThreadSafe
public class SynchronizedCollaborativeDocumentProvider implements CollaborativeDocumentProvider {

    private AtomicInteger idSequence = new AtomicInteger(1);

    private Map<String, CollaborativeDocument> documents = new ConcurrentHashMap<>();
    private OperationRebaser operationRebaser;

    @Inject
    public SynchronizedCollaborativeDocumentProvider(OperationRebaser operationRebaser) {
        this.operationRebaser = operationRebaser;
    }

    @Override
    public CollaborativeDocument getById(String documentId) {
        return documents.get(documentId);
    }

    @Override
    public Iterable<CollaborativeDocument> getAllDocuments() {
        return documents.values();
    }

    @Override
    public CollaborativeDocument create(String title) {
        String id = generateId();
        SynchronizedDocument document = createDocument(id, title);
        documents.put(id, document);
        return document;
    }

    private String generateId() {
        int currentId = idSequence.getAndIncrement();
        return String.valueOf(currentId);
    }

    private SynchronizedDocument createDocument(String id, String title) {
        DocumentContent content = new StringDocumentContent();
        return new SynchronizedDocument(id, title, content, operationRebaser);
    }
}
