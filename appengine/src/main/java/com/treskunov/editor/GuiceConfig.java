package com.treskunov.editor;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.treskunov.editor.channel.ChannelApiCollaboratorsProvider;
import com.treskunov.editor.channel.InMemoryChannelApiCollaboratorsProvider;
import com.treskunov.editor.document.InMemoryCollaborativeDocumentRepository;
import com.treskunov.editor.operation.ActionTransformer;
import com.treskunov.editor.operation.OperationRebaser;
import com.treskunov.editor.operation.SimpleActionTransformer;
import com.treskunov.editor.operation.TransformingOperationRebaser;
import com.treskunov.editor.servlet.FormOperationParser;
import com.treskunov.editor.servlet.OperationParser;
import com.treskunov.editor.servlet.ServletConfig;

//TODO read about Guice configuration
public class GuiceConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        Injector injector = Guice.createInjector(new CollaborativeEditorModule(), new ServletConfig());
        createDocumentForTest(injector);
        return injector;
    }

    private void createDocumentForTest(Injector injector) {
        CollaborativeDocumentRepository repository = injector.getInstance(CollaborativeDocumentRepository.class);
        repository.create("Hello world!");
    }

    private static class CollaborativeEditorModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(CollaborativeDocumentRepository.class).to(InMemoryCollaborativeDocumentRepository.class);
            bind(OperationParser.class).to(FormOperationParser.class);
            bind(ActionTransformer.class).to(SimpleActionTransformer.class);
            bind(OperationRebaser.class).to(TransformingOperationRebaser.class);
            bind(ChannelApiCollaboratorsProvider.class).to(InMemoryChannelApiCollaboratorsProvider.class);
        }
    }

}
