package com.treskunov.editor;

import com.treskunov.editor.operation.action.DeleteAction;
import com.treskunov.editor.operation.action.InsertAction;
import com.treskunov.editor.operation.action.NoOpAction;

public interface ActionVisitor {

    void visit(InsertAction insertAction);

    void visit(DeleteAction deleteAction);

    void visit(NoOpAction noOpAction);
}
