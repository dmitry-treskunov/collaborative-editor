package com.treskunov.editor.operation;

import com.treskunov.editor.Action;
import com.treskunov.editor.ActionVisitor;
import com.treskunov.editor.DocumentContent;
import com.treskunov.editor.Operation;
import com.treskunov.editor.operation.action.NoOpAction;

import java.util.Objects;

public class ActingOperation implements Operation {

    private final Action action;
    private final int version;
    private final String initiator;

    /**
     * Use {@link OperationBuilder} to build operation.
     */
    private ActingOperation(String initiator, int version, Action action) {
        this.initiator = initiator;
        this.version = version;
        this.action = action;
    }

    @Deprecated
    public ActingOperation(String dmitry, int i, String s, int i1) {
        //To change body of created methods use File | Settings | File Templates.
        action = null;
        version = 0;
        initiator = null;
    }

    @Deprecated
    public ActingOperation(int i, String s) {
        action = null;
        version = 0;
        initiator = null;
    }

    @Override
    public void applyTo(DocumentContent document) {
        action.applyTo(document);
    }

    @Override
    public String getInitiator() {
        return initiator;
    }

    @Override
    public int getDocumentVersion() {
        return version;
    }

    @Override
    public Action getAction() {
        return action;
    }

    @Override
    public void accept(ActionVisitor visitor) {
        action.accept(visitor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, version, initiator);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ActingOperation other = (ActingOperation) obj;
        return Objects.equals(this.action, other.action) && Objects.equals(this.version, other.version) && Objects.equals(this.initiator, other.initiator);
    }

    @Override
    public String toString() {
        return "Operation by '" + initiator + "' on version '" + version + "' with action: " + action.toString();
    }

    public static final class OperationBuilder {

        private String initiator;
        private int version;

        private OperationBuilder(String initiator) {
            this.initiator = initiator;
        }

        public static OperationBuilder anOperationBy(String initiator) {
            return new OperationBuilder(initiator);
        }

        public OperationBuilder onDocumentVersion(int version) {
            this.version = version;
            return this;
        }

        public Operation withAction(Action action) {
            return new ActingOperation(initiator, version, action);
        }

        public Operation withoutAction() {
            return new ActingOperation(initiator, version, new NoOpAction());
        }
    }
}
