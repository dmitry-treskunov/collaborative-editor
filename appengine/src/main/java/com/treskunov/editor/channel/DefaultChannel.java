package com.treskunov.editor.channel;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelServiceFactory;

import java.util.Objects;

/**
 * Default implementation of {@link com.treskunov.editor.channel.Channel} that works with App Engine 1.8.6.
 */
public class DefaultChannel implements Channel {

    private String clientId;

    public DefaultChannel(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String init() {
        String token = ChannelServiceFactory.getChannelService().createChannel(clientId);
        return token;
    }

    @Override
    public void sendMessage(String message) {
        ChannelMessage channelMessage = new ChannelMessage(clientId, message);
        ChannelServiceFactory.getChannelService().sendMessage(channelMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final DefaultChannel other = (DefaultChannel) obj;
        return Objects.equals(this.clientId, other.clientId);
    }

    @Override
    public String toString() {
        return "DefaultChannel{" +
                "clientId='" + clientId + '\'' +
                '}';
    }
}
