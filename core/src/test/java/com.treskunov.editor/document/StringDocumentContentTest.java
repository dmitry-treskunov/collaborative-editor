package com.treskunov.editor.document;

import org.hamcrest.core.Is;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;

public class StringDocumentContentTest {

    @Test
    public void shouldBeInitializedWithEmptyText() throws Exception {
        StringDocumentContent content = new StringDocumentContent();

        assertThat(content.asText(), is(""));
    }

    @Test
    public void shouldBeInitializedAsEmpty() throws Exception {
        StringDocumentContent content = new StringDocumentContent();

        assertThat(content.isEmpty(), is(true));
    }

    @Test
    public void shouldSaveTextPassedInConstructor() throws Exception {
        StringDocumentContent content = new StringDocumentContent("Hello world!");

        assertThat(content.asText(), is("Hello world!"));
    }

    @Test
    public void shouldBeNonEmptyWhenInitializedWithText() throws Exception {
        StringDocumentContent content = new StringDocumentContent("Hello world!");

        assertThat(content.isEmpty(), is(false));
    }

    @Test
    public void shouldBeNonEmptyAfterInsertion() throws Exception {
        StringDocumentContent content = new StringDocumentContent();

        content.insert(0, "Hello!");

        assertThat(content.isEmpty(), is(false));
    }

    @Test
    public void shouldUpdateTextOnInsertInEmptyDocument() throws Exception {
        StringDocumentContent content = new StringDocumentContent();

        content.insert(0, "Hello");

        assertThat(content.asText(), is("Hello"));
    }

    @Test
    public void shouldUpdateTextOnInsertInTheEndOfNonEmptyDocument() throws Exception {
        StringDocumentContent content = new StringDocumentContent("Hello");

        content.insert(5, " world");

        assertThat(content.asText(), is("Hello world"));
    }

    @Test
    public void shouldUpdateTextOnInsertInTheMiddleOfNonEmptyDocument() throws Exception {
        StringDocumentContent content = new StringDocumentContent("Hllo world");

        content.insert(1, "e");

        assertThat(content.asText(), is("Hello world"));
    }

    @Test
    public void shouldUpdateTextOnInsertInTheStartOfNonEmptyDocument() throws Exception {
        StringDocumentContent content = new StringDocumentContent("world");

        content.insert(0, "Hello ");

        assertThat(content.asText(), is("Hello world"));
    }

    @Test
    public void shouldUpdateTextOnDeleteFromTheEnd() throws Exception {
        StringDocumentContent content = new StringDocumentContent("Hello world");

        content.delete(5, 6);

        assertThat(content.asText(), is("Hello"));
    }

    @Test
    public void shouldUpdateTextOnDeleteFromTheStart() throws Exception {
        StringDocumentContent content = new StringDocumentContent("Hello world");

        content.delete(0, 6);

        assertThat(content.asText(), is("world"));
    }

    @Test
    public void shouldUpdateTextOnDeleteFromTheMiddle() throws Exception {
        StringDocumentContent content = new StringDocumentContent("Hello my world");

        content.delete(6, 3);

        assertThat(content.asText(), is("Hello world"));
    }

    @Test
    public void shouldUpdateTextOnDeleteOneCharacterFromTheMiddle() throws Exception {
        StringDocumentContent content = new StringDocumentContent("Hello, world");

        content.delete(5, 1);

        assertThat(content.asText(), is("Hello world"));
    }

    @Test
    public void shouldBeEmptyAfterDeleteAllContent() throws Exception {
        StringDocumentContent content = new StringDocumentContent("Hello");

        content.delete(0, 5);

        assertThat(content.isEmpty(), is(true));
    }
}
