package com.treskunov.editor.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.treskunov.editor.channel.ChannelApiCollaboratorsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet loading document editor.
 * </p>
 * It's not correct to pass document content to the client in this servlet, because
 * client should first open channel to server (for listening document updates)
 * and only after that ask for document content.
 * Otherwise it's possible that client will miss some updates.
 */
@Singleton
public class DocumentEditorServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DocumentEditorServlet.class);
    private ChannelApiCollaboratorsProvider collaboratorsProvider;

    @Inject
    public DocumentEditorServlet(ChannelApiCollaboratorsProvider collaboratorsProvider) {
        this.collaboratorsProvider = collaboratorsProvider;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String documentId = getDocumentId(request);
        String clientId = collaboratorsProvider.create();
        request.setAttribute("clientId", clientId);
        request.setAttribute("documentId", documentId);
        request.getRequestDispatcher("/views/editor.jsp").include(request, response);
    }

    private String getDocumentId(HttpServletRequest request) {
        String documentId = request.getParameter("documentId");
        logger.debug("Request to open editor for document with id {}", documentId);
        return documentId;
    }
}
