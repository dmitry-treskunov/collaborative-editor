package com.treskunov.editor;

public interface MutableDocument extends Document {

    void apply(Operation operation);
}
