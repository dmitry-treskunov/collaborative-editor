package com.treskunov.editor.channel;

import com.treskunov.editor.ActionVisitor;
import com.treskunov.editor.Collaborator;
import com.treskunov.editor.Operation;
import com.treskunov.editor.operation.action.DeleteAction;
import com.treskunov.editor.operation.action.InsertAction;
import com.treskunov.editor.operation.action.NoOpAction;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * {@link Collaborator} implementation for App Engine Channel API.
 */
public class ChannelApiCollaborator implements Collaborator {

    private final Channel channel;
    private final String documentId;

    public ChannelApiCollaborator(Channel channel, String documentId) {
        this.channel = channel;
        this.documentId = documentId;
    }

    @Override
    public String getDocumentId() {
        return documentId;
    }

    @Override
    public void onOperationApplied(Operation operation) {
        String message = serialize(operation);
        channel.sendMessage(message);
    }

    public String initChannel() {
        return channel.init();
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel, documentId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ChannelApiCollaborator other = (ChannelApiCollaborator) obj;
        return Objects.equals(this.channel, other.channel);
    }

    @Override
    public String toString() {
        return "ChannelApiCollaborator{" +
                "channel=" + channel +
                ", documentId='" + documentId + '\'' +
                '}';
    }

    private String serialize(Operation operation) {
        SerializerVisitor serializerVisitor = new SerializerVisitor(operation);
        operation.accept(serializerVisitor);
        return serializerVisitor.getMessage();
    }

    /**
     * Serialize {@link Operation} to json using Visitor pattern for
     * serializing concrete implementations of {@link com.treskunov.editor.Action}
     */
    private static final class SerializerVisitor implements ActionVisitor {

        private String message;
        private Operation operation;

        public SerializerVisitor(Operation operation) {
            this.operation = operation;
        }

        @Override
        public void visit(InsertAction insertAction) {
            Map<String, Object> fields = commonOperationsFields();
            fields.put("op", "insert");
            fields.put("position", insertAction.getIndex());
            fields.put("value", insertAction.getValueToInsert());
            this.message = new JSONObject(fields).toString();
        }

        @Override
        public void visit(DeleteAction deleteAction) {
            Map<String, Object> messageFields = commonOperationsFields();
            messageFields.put("op", "delete");
            messageFields.put("position", deleteAction.getIndex());
            messageFields.put("value", deleteAction.getValueToDelete());
            this.message = new JSONObject(messageFields).toString();
        }

        @Override
        public void visit(NoOpAction noOpAction) {
            Map<String, Object> messageFields = commonOperationsFields();
            messageFields.put("op", "noop");
            this.message = new JSONObject(messageFields).toString();
        }

        private Map<String, Object> commonOperationsFields() {
            Map<String, Object> fields = new LinkedHashMap<>();
            fields.put("version", operation.getDocumentVersion());
            fields.put("initiator", operation.getInitiator());
            return fields;
        }

        String getMessage() {
            return message;
        }
    }
}
