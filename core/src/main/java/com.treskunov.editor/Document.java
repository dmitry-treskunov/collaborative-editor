package com.treskunov.editor;

public interface Document {

    String getId();

    String getTitle();

    String asText();

    void apply(Operation operation);
}
