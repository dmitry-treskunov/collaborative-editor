package com.treskunov.editor.document;

import com.treskunov.editor.CollaborativeDocument;
import com.treskunov.editor.operation.OperationRebaser;
import org.hamcrest.collection.IsIterableWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.text.IsEmptyString.isEmptyString;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryCollaborativeDocumentRepositoryTest {

    @Mock
    private OperationRebaser operationRebaser;

    @InjectMocks
    private InMemoryCollaborativeDocumentRepository repository;

    @Test
    public void shouldStoreDocument() throws Exception {
        CollaborativeDocument created = repository.create("Hello world");

        CollaborativeDocument found = repository.getById(created.getId());

        assertThat(found, is(created));
    }

    @Test
    public void shouldReturnNullWhenDocumentNotExist() throws Exception {
        CollaborativeDocument document = repository.getById("nonExistingId");

        assertThat(document, is(nullValue()));
    }

    @Test
    public void shouldReturnAllSavedDocuments() throws Exception {
        CollaborativeDocument first = repository.create("First");
        CollaborativeDocument second = repository.create("Second");

        Iterable<CollaborativeDocument> foundDocuments = repository.getAllDocuments();

        assertThat(foundDocuments, containsInAnyOrder(first, second));
    }

    @Test
    public void shouldReturnEmptyCollectionWhenThereAreNoDocuments() throws Exception {
        Iterable<CollaborativeDocument> foundDocuments = repository.getAllDocuments();

        assertThat(foundDocuments, IsIterableWithSize.<CollaborativeDocument>iterableWithSize(0));
    }

    @Test
    public void shouldCreateDocumentsWithDifferentIds() throws Exception {
        CollaborativeDocument first = repository.create("First");
        CollaborativeDocument second = repository.create("Second");

        assertThat(first.getId(), is(not(equalTo(second.getId()))));
    }

    @Test
    public void shouldCreateDocumentsWithSpecifiedTitle() throws Exception {
        CollaborativeDocument created = repository.create("Hello!");

        assertThat(created.getTitle(), is("Hello!"));
    }

    @Test
    public void shouldCreateEmptyDocument() throws Exception {
        CollaborativeDocument created = repository.create("Hello!");

        assertThat(created.asText(), is(isEmptyString()));
    }
}
