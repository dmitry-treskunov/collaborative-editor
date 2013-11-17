package com.treskunov.editor.channel;

public interface ChannelApiCollaboratorsProvider {

    /**
     * Create collaborator for document with specified id.
     */
    String create(String documentId);

    /**
     * Each {@link ChannelApiCollaborator} is attached to the clientId.
     */
    ChannelApiCollaborator getByClientId(String clientId);
}
