package com.treskunov.editor.document;

import com.treskunov.editor.Operation;
import com.treskunov.editor.operation.OperationRebaser;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * This is a sketch
 */
public class ProducersConsumerDocument extends AbstractCollaborativeDocument {

    //TODO extract it to configuration
    private static final int CAPACITY = 1000;

    private BlockingQueue<Operation> waitingOperations = new ArrayBlockingQueue<>(CAPACITY);

    private List<Operation> appliedOperations;
    private OperationRebaser operationRebaser;

    private int version;

    public ProducersConsumerDocument(OperationRebaser operationRebaser) {
        this.operationRebaser = operationRebaser;
    }

    @Override
    public void apply(Operation operation) {
        //init worker here
        try {
            waitingOperations.put(operation);
        } catch (InterruptedException ex) {
            //handle
        }
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public String asText() {
        //lazily apply operations and return result
        return null;
    }

    @Override
    public String getTitle() {
        //return title here
        return null;
    }

    @Override
    public String getId() {
        //return id here
        return null;
    }

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
            ProducersConsumerDocument.this.version++;
            notifyCollaborators(operationToApply);
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
            return ProducersConsumerDocument.this.getVersion() > operation.getDocumentVersion();
        }
    }
}
