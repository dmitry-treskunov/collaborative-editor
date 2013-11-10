package com.treskunov.editor.channel;

import com.google.inject.Singleton;
import net.jcip.annotations.ThreadSafe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
@ThreadSafe
public class InMemoryChannelApiCollaboratorsProvider implements ChannelApiCollaboratorsProvider {

    private AtomicInteger clientIdSequence = new AtomicInteger();
    private Map<String, ChannelApiCollaborator> collaborators = new ConcurrentHashMap<>();

    @Override
    public String create() {
        String clientId = String.valueOf(clientIdSequence.getAndIncrement());
        ChannelApiCollaborator collaborator = createCollaborator(clientId);
        collaborators.put(clientId, collaborator);
        return clientId;
    }

    @Override
    public ChannelApiCollaborator getByClientId(String clientId) {
        return collaborators.get(clientId);
    }

    private ChannelApiCollaborator createCollaborator(String clientId) {
        Channel channel = new DefaultChannel(clientId);
        return new ChannelApiCollaborator(channel);
    }

}
