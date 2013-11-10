package com.treskunov.editor.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.treskunov.editor.CollaborativeDocument;
import com.treskunov.editor.CollaborativeDocumentRepository;
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

    private static final String JSON_CONTENT_TYPE = "application/json";

    private CollaborativeDocumentRepository documentRepository;
    private ChannelApiCollaboratorsProvider collaboratorsProvider;

    @Inject
    public GetDocumentServlet(CollaborativeDocumentRepository documentRepository, ChannelApiCollaboratorsProvider collaboratorsProvider) {
        this.documentRepository = documentRepository;
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
        putDocumentToResponse(resp, document);
    }

    private CollaborativeDocument findDocument(HttpServletRequest req) {
        String documentId = req.getParameter("documentId");
        return documentRepository.getById(documentId);
    }

    private void registerCollaborator(HttpServletRequest req, CollaborativeDocument document) {
        String clientId = req.getParameter("clientId");
        ChannelApiCollaborator collaborator = collaboratorsProvider.getByClientId(clientId);
        document.registerCollaborator(collaborator);
    }

    private void putDocumentToResponse(HttpServletResponse resp, CollaborativeDocument document) throws IOException {
        resp.setContentType(JSON_CONTENT_TYPE);
        Map<String, Object> responseContent = new LinkedHashMap<>();
        responseContent.put("documentTitle", document.getTitle());
        responseContent.put("documentVersion", document.getVersion());
        responseContent.put("documentText", document.asText());
        String contentAsJson = new JSONObject(responseContent).toString();
        resp.getWriter().print(contentAsJson);
    }
}
