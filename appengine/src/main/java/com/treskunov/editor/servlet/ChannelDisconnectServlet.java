package com.treskunov.editor.servlet;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.treskunov.editor.CollaborativeDocument;
import com.treskunov.editor.CollaborativeDocumentProvider;
import com.treskunov.editor.Collaborator;
import com.treskunov.editor.channel.ChannelApiCollaborator;
import com.treskunov.editor.channel.ChannelApiCollaboratorsProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This servlet provides endpoint that is called by AppEngine platform when
 * the channel is disconnected.
 */
@Singleton
public class ChannelDisconnectServlet extends HttpServlet {

    private ChannelApiCollaboratorsProvider channelApiCollaboratorsProvider;
    private CollaborativeDocumentProvider documentProvider;

    @Inject
    public ChannelDisconnectServlet(ChannelApiCollaboratorsProvider channelApiCollaboratorsProvider, CollaborativeDocumentProvider documentProvider) {
        this.channelApiCollaboratorsProvider = channelApiCollaboratorsProvider;
        this.documentProvider = documentProvider;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collaborator collaborator = fetchDisconnectedCollaborator(req);
        unregister(collaborator);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * TODO Create Gateway for Channel API interactions.
     */
    private ChannelApiCollaborator fetchDisconnectedCollaborator(HttpServletRequest req) throws IOException {
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        String clientId = channelService.parsePresence(req).clientId();
        return channelApiCollaboratorsProvider.getByClientId(clientId);
    }

    private void unregister(Collaborator collaborator) {
        CollaborativeDocument document = documentProvider.getById(collaborator.getDocumentId());
        document.unregisterCollaborator(collaborator);
    }
}
