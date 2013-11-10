package com.treskunov.editor.channel;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelServiceFactory;

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
}
