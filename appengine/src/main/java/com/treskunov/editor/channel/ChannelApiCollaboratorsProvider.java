package com.treskunov.editor.channel;

public interface ChannelApiCollaboratorsProvider {

    String create();

    ChannelApiCollaborator getByClientId(String clientId);
}
