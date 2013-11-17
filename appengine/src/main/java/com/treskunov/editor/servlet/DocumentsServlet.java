package com.treskunov.editor.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.treskunov.editor.CollaborativeDocument;
import com.treskunov.editor.CollaborativeDocumentProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Provides 'getDocuments' and 'createDocument' endpoints.
 */
@Singleton
public class DocumentsServlet extends HttpServlet {

    private CollaborativeDocumentProvider documentProvider;

    @Inject
    public DocumentsServlet(CollaborativeDocumentProvider documentProvider) {
        this.documentProvider = documentProvider;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Iterable<CollaborativeDocument> existingDocuments = documentProvider.getAllDocuments();
        req.setAttribute("documents", existingDocuments);
        req.getRequestDispatcher("/views/documents.jsp").include(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        CollaborativeDocument created = documentProvider.create(title);
        resp.sendRedirect("/editor?documentId=" + created.getId());
    }
}
