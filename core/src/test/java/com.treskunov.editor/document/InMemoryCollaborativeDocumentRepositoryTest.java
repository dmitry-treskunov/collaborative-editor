package com.treskunov.editor.document;

import com.treskunov.editor.CollaborativeDocument;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class InMemoryCollaborativeDocumentRepositoryTest {

    private InMemoryCollaborativeDocumentRepository repository = new InMemoryCollaborativeDocumentRepository(null);

    @Test
    public void shouldStoreDocument() throws Exception {
        CollaborativeDocument document = mock(CollaborativeDocument.class);

        repository.save("document1", document);

        assertThat(repository.getById("document1"), is(document));
    }

    @Test
    public void shouldReturnNullWhenDocumentNotExist() throws Exception {
        CollaborativeDocument document = repository.getById("nonExistingId");

        assertThat(document, is(nullValue()));
    }
}
