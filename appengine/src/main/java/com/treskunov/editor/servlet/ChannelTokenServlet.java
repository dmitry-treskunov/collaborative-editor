package com.treskunov.editor.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.treskunov.editor.channel.ChannelApiCollaborator;
import com.treskunov.editor.channel.ChannelApiCollaboratorsProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The following scenario seems to be most logical for Channel Api interaction:
 * <ul>
 * <li>Client requests page with document editor using {@link DocumentEditorServlet}
 * <li>{@link DocumentEditorServlet} puts 'clientId' to the returned page</li>
 * <li>Using XHR client asks this Servlet for token passing 'clientId' in the request</li>
 * <li>Client opens channel using received token</li>
 * </ul>
 * <p/>
 * <p/>
 * NOTE: this servlet also should be used to re-initialize expired tokens.
 * Currently token is expired in two hours.
 */
@Singleton
public class ChannelTokenServlet extends HttpServlet {

    private ChannelApiCollaboratorsProvider collaboratorsProvider;

    @Inject
    public ChannelTokenServlet(ChannelApiCollaboratorsProvider collaboratorsProvider) {
        this.collaboratorsProvider = collaboratorsProvider;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ChannelApiCollaborator collaborator = findCollaborator(req);
        String token = collaborator.initChannel();
        respondWithToken(resp, token);
    }

    private ChannelApiCollaborator findCollaborator(HttpServletRequest req) {
        String clientId = req.getParameter("clientId");
        return collaboratorsProvider.getByClientId(clientId);
    }

    private void respondWithToken(HttpServletResponse resp, String token) throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().print(token);
    }
}
