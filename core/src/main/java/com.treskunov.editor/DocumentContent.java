package com.treskunov.editor;

public interface DocumentContent {

    void insert(int index, String valueToInsert);

    void delete(int start, int count);

    boolean isEmpty();

    String asText();
}
