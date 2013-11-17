package com.treskunov.editor.document;

import com.treskunov.editor.DocumentSnapshot;
import com.treskunov.editor.Operation;
import com.treskunov.editor.operation.OperationRebaser;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The sketch of the implementation based on producers-consumers pattern:
 * - requests are producers
 * - internal thread is single consumer that processes operations consequentially.
 * <p/>
 * This approach can reduce contention between request threads.
 * <p/>
 * Also this implementation could use list of applied operations as internal
 * representation of content. When client asks for the content, then operations are
 * applied and result can be cached.
 */
public class ProducersConsumerDocument extends AbstractCollaborativeDocument {

    private static final int CAPACITY = 1000;
    private BlockingQueue<Operation> waitingOperations = new ArrayBlockingQueue<>(CAPACITY);

    private List<Operation> appliedOperations;
    private OperationRebaser operationRebaser;
    private AtomicInteger version;
    private String title;

    public ProducersConsumerDocument(OperationRebaser operationRebaser) {
        this.operationRebaser = operationRebaser;
    }

    @Override
    public void apply(Operation operation) {
        //init worker thread here
        try {
            waitingOperations.put(operation);
        } catch (InterruptedException ex) {
            //handle
        }
    }

    @Override
    public int getVersion() {
        return version.get();
    }

    @Override
    public DocumentSnapshot getSnapshot() {
        //lazily apply operations from buffer and return result
        return null;
    }

    @Override
    public String asText() {
        //lazily apply operations from buffer and return result
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getId() {
        //return id here
        return null;
    }

    /**
     * Fetches one operation from the queue and process it.
     */
    private class Worker implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    apply(waitingOperations.take());
                }
            } catch (InterruptedException ex) {
                //handle
            }
        }

        private void apply(Operation operation) {
            Operation operationToApply = rebase(operation);
            appliedOperations.add(operationToApply);
            //we don't want to execute operation immediately, we just remember it.
            ProducersConsumerDocument.this.version.incrementAndGet();
            notifyCollaborators(operationToApply);
        }

        private synchronized Operation rebase(Operation operation) {
            if (hasOldVersion(operation)) {
                int gapSize = version.get() - operation.getDocumentVersion();
                List<Operation> history = appliedOperations.subList(appliedOperations.size() - gapSize, appliedOperations.size());
                return operationRebaser.rebase(operation, history);
            }
            return operation;
        }

        private boolean hasOldVersion(Operation operation) {
            return ProducersConsumerDocument.this.getVersion() > operation.getDocumentVersion();
        }
    }
}
