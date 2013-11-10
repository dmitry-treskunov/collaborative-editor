package com.treskunov.editor.channel;

/**
 * Gateway hides low-level interactions with App Engine Channel API.
 * <p/>
 * Also it's very suitable for unit testing of classes that require
 * Channel API interactions, because Channel API itself provides static methods
 * that throw exceptions when are called outside of app engine server.
 *
 * @see com.google.appengine.api.channel.ChannelServiceFactory
 * @see com.google.appengine.api.channel.ChannelService
 */
public interface Channel {

    /**
     * Get client id associated with the Channel
     */
    String getClientId();

    /**
     * Initialize/re-initialize a channel.
     * This method returns the token that client should use to connect to this channel.
     * The token is expired in two hours and should be re-initialized using this method.
     *
     * @return the token that client will use to connect to this channel.
     */
    String init();

    /**
     * Sends a message to the channel.
     */
    void sendMessage(String message);
}
