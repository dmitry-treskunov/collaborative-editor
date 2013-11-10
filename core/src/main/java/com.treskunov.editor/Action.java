package com.treskunov.editor;

public interface Action {

    void applyTo(DocumentContent content);

    void accept(ActionVisitor visitor);
}
