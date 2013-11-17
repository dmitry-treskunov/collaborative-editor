package com.treskunov.editor.document;

import com.treskunov.editor.DocumentContent;
import net.jcip.annotations.NotThreadSafe;

import java.util.Objects;

/**
 * The simplest but non optimal implementation of {@link DocumentContent}
 * backed by string.
 * <p/>
 * Note that all operations are very expensive.
 */
@NotThreadSafe
public class StringDocumentContent implements DocumentContent {
    private String content;

    public StringDocumentContent() {
        this("");
    }

    public StringDocumentContent(String content) {
        this.content = content;
    }

    @Override
    public void insert(int index, String valueToInsert) {
        content = content.substring(0, index) + valueToInsert + content.substring(index);
    }

    @Override
    public void delete(int start, int count) {
        content = content.substring(0, start) + content.substring(start + count);
    }

    @Override
    public boolean isEmpty() {
        return content.isEmpty();
    }

    @Override
    public String asText() {
        return content;
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final StringDocumentContent other = (StringDocumentContent) obj;
        return Objects.equals(this.content, other.content);
    }

    @Override
    public String toString() {
        return content;
    }
}
