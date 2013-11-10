package com.treskunov.editor.operation.action;

import com.treskunov.editor.Action;
import com.treskunov.editor.ActionVisitor;
import com.treskunov.editor.DocumentContent;

public class NoOpAction implements Action {

    @Override
    public void applyTo(DocumentContent content) {
        //do nothing
    }

    @Override
    public void accept(ActionVisitor visitor) {
        visitor.visit(this);
    }
}
