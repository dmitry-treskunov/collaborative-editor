package com.treskunov.editor.operation;

import com.treskunov.editor.Operation;

import java.util.List;

/**
 * Any operation, that is supposed to be applied to the document, should be rebased
 * on the latest version of document first.
 * <p/>
 * That is, if, for example, operation was assumed to be applied on document version '1'
 * but current version of document is '4', then this operation should be rebased against
 * changes introduced by operations on version '2' and '3'.
 */
public interface OperationRebaser {

    Operation rebase(Operation current, List<Operation> history);
}
