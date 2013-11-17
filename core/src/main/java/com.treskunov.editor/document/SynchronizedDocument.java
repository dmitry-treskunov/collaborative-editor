package com.treskunov.editor.document;

import com.treskunov.editor.DocumentContent;
import com.treskunov.editor.DocumentSnapshot;
import com.treskunov.editor.Operation;
import com.treskunov.editor.operation.OperationRebaser;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplest implementation of {@link com.treskunov.editor.CollaborativeDocument}:
 * <ul>
 * <li>All methods are synchronized (can be cause of high contention)</li>
 * <li>All operations are applied eagerly</li>
 * </ul>
 *
 * @see ProducersConsumerDocument
 */
@ThreadSafe
public class SynchronizedDocument extends AbstractCollaborativeDocument {

    @GuardedBy("this")
    private DocumentContent content;
    @GuardedBy("this")
    private int version;
    @GuardedBy("this")
    private List<Operation> appliedOperations = new ArrayList<>();
    private OperationRebaser operationRebaser;
    private final String title;
    private final String id;

    public SynchronizedDocument(String id, String title, DocumentContent content, OperationRebaser operationRebaser) {
        this.id = id;
        this.content = content;
        this.title = title;
        this.operationRebaser = operationRebaser;
    }

    @Override
    public synchronized void apply(Operation operation) {
        Operation operationToApply = rebase(operation);
        applyOperation(operationToApply);
        notifyCollaborators(operationToApply);
    }

    @Override
    public synchronized String asText() {
        return content.asText();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public synchronized int getVersion() {
        return version;
    }

    @Override
    public synchronized DocumentSnapshot getSnapshot() {
        return new DocumentSnapshot(title, content.asText(), version);
    }

    @Override
    public String toString() {
        return asText();
    }

    private synchronized Operation rebase(Operation operation) {
        if (hasOldVersion(operation)) {
            int gapSize = version - operation.getDocumentVersion();
            List<Operation> history = appliedOperations.subList(appliedOperations.size() - gapSize, appliedOperations.size());
            return operationRebaser.rebase(operation, history);
        }
        return operation;
    }

    private boolean hasOldVersion(Operation operation) {
        return this.getVersion() > operation.getDocumentVersion();
    }

    private synchronized void applyOperation(Operation operation) {
        operation.applyTo(this.content);
        appliedOperations.add(operation);
        version++;
    }
}
