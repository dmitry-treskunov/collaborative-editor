package com.treskunov.editor;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.treskunov.editor.channel.ChannelApiCollaboratorsProvider;
import com.treskunov.editor.channel.InMemoryChannelApiCollaboratorsProvider;
import com.treskunov.editor.document.SynchronizedCollaborativeDocumentProvider;
import com.treskunov.editor.operation.ActionTransformer;
import com.treskunov.editor.operation.OperationRebaser;
import com.treskunov.editor.operation.SimpleActionTransformer;
import com.treskunov.editor.operation.TransformingOperationRebaser;
import com.treskunov.editor.servlet.*;

public class GuiceConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        Injector injector = Guice.createInjector(new CollaborativeEditorModule(), new ServletConfig());
        createDocumentForTest(injector);
        return injector;
    }

    /**
     * Initialize application with one document.
     */
    private void createDocumentForTest(Injector injector) {
        CollaborativeDocumentProvider documentProvider = injector.getInstance(CollaborativeDocumentProvider.class);
        documentProvider.create("Hello world!");
    }

    private static class CollaborativeEditorModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(CollaborativeDocumentProvider.class).to(SynchronizedCollaborativeDocumentProvider.class);
            bind(OperationParser.class).to(FormOperationParser.class);
            bind(ActionTransformer.class).to(SimpleActionTransformer.class);
            bind(OperationRebaser.class).to(TransformingOperationRebaser.class);
            bind(ChannelApiCollaboratorsProvider.class).to(InMemoryChannelApiCollaboratorsProvider.class);
        }
    }

    public static class ServletConfig extends ServletModule {
        @Override
        protected void configureServlets() {
            serve("/").with(DocumentsServlet.class);
            serve("/editor").with(DocumentEditorServlet.class);
            serve("/channel/token").with(ChannelTokenServlet.class);
            serve("/document").with(GetDocumentServlet.class);
            serve("/document/update").with(UpdateOperationServlet.class);
        }
    }
}
