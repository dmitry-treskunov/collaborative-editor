package com.treskunov.editor.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.treskunov.editor.CollaborativeDocumentRepository;
import com.treskunov.editor.MutableDocument;
import com.treskunov.editor.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet processing update document operations.
 * <p/>
 * NOTE: ideally, this servlet should be mapped to requests like this:
 * <pre>
 * PATCH /api/documents/{documentId}/
 * {
 *     //operation json here
 * }
 * </pre>
 * But it is harder to parse using native Servlet API than requests containing
 * operation in request parameters.
 */
@Singleton
public class UpdateOperationServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UpdateOperationServlet.class);

    private final OperationParser parser;
    private final CollaborativeDocumentRepository documentRepository;

    @Inject
    public UpdateOperationServlet(OperationParser parser, CollaborativeDocumentRepository documentRepository) {
        this.parser = parser;
        this.documentRepository = documentRepository;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Operation operation = parseOperation(req);
        MutableDocument document = findDocument(req);
        document.apply(operation);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private Operation parseOperation(HttpServletRequest req) {
        Operation operation = parser.fromRequest(req);
        logger.info("Operation received {}", operation);
        return operation;
    }

    private MutableDocument findDocument(HttpServletRequest req) {
        String documentId = req.getParameter("documentId");
        return documentRepository.getById(documentId);
    }
}
