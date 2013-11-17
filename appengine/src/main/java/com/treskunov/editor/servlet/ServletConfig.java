package com.treskunov.editor.servlet;

import com.google.inject.servlet.ServletModule;

public class ServletConfig extends ServletModule {
    @Override
    protected void configureServlets() {
        serve("/").with(DocumentsServlet.class);
        serve("/editor").with(DocumentEditorServlet.class);
        serve("/channel/token").with(ChannelTokenServlet.class);
        serve("/document").with(GetDocumentServlet.class);
        serve("/document/update").with(UpdateOperationServlet.class);
    }
}
