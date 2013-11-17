package com.treskunov.editor.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.treskunov.editor.CollaborativeDocument;
import com.treskunov.editor.CollaborativeDocumentProvider;
import com.treskunov.editor.DocumentSnapshot;
import com.treskunov.editor.channel.ChannelApiCollaborator;
import com.treskunov.editor.channel.ChannelApiCollaboratorsProvider;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This servlet provides endpoint for fetching current version of document
 * by the client.
 * <p/>
 * NOTE: ideally, this servlet should be mapped to requests like this:
 * <pre>
 * GET /api/documents/{documentId}/
 * </pre>
 * But it is a little harder to parse using native Servlet API than requests like this:
 * <pre>
 * GET /document?documentId={documentId}
 * </pre>
 */
@Singleton
public class GetDocumentServlet extends HttpServlet {

    private CollaborativeDocumentProvider documentProvider;
    private ChannelApiCollaboratorsProvider collaboratorsProvider;

    @Inject
    public GetDocumentServlet(CollaborativeDocumentProvider documentProvider, ChannelApiCollaboratorsProvider collaboratorsProvider) {
        this.documentProvider = documentProvider;
        this.collaboratorsProvider = collaboratorsProvider;
    }

    /**
     * Returns current version of the document.
     * <p/>
     * Format of supported request:
     * <pre>
     * GET /document?documentId={documentId}&clientId={clientId}
     * </pre>
     * <p/>
     * Response body is json containing the following fields:
     * <ul>
     * <li>documentTitle</li>
     * <li>documentVersion</li>
     * <li>documentText</li>
     * </ul>
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CollaborativeDocument document = findDocument(req);
        registerCollaborator(req, document);
        putDocumentSnapshotToResponse(resp, document.getSnapshot());
    }

    private CollaborativeDocument findDocument(HttpServletRequest req) {
        String documentId = req.getParameter("documentId");
        return documentProvider.getById(documentId);
    }

    private void registerCollaborator(HttpServletRequest req, CollaborativeDocument document) {
        String clientId = req.getParameter("clientId");
        ChannelApiCollaborator collaborator = collaboratorsProvider.getByClientId(clientId);
        document.registerCollaborator(collaborator);
    }

    private void putDocumentSnapshotToResponse(HttpServletResponse resp, DocumentSnapshot snapshot) throws IOException {
        resp.setContentType(ContentType.APPLICATION_JSON);
        Map<String, Object> responseContent = new LinkedHashMap<>();
        responseContent.put("documentTitle", snapshot.getTitle());
        responseContent.put("documentVersion", snapshot.getVersion());
        responseContent.put("documentText", snapshot.getText());
        String contentAsJson = new JSONObject(responseContent).toString();
        resp.getWriter().print(contentAsJson);
    }
}
