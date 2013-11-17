package com.treskunov.editor.operation;


import com.google.inject.Singleton;
import com.treskunov.editor.Action;
import com.treskunov.editor.operation.action.DeleteAction;
import com.treskunov.editor.operation.action.InsertAction;
import com.treskunov.editor.operation.action.NoOpAction;
import net.jcip.annotations.ThreadSafe;

import static com.treskunov.editor.operation.action.DeleteAction.DeleteActionBuilder.delete;
import static com.treskunov.editor.operation.action.InsertAction.InsertActionBuilder.insert;

/**
 * This is the simplest version of {@link ActionTransformer} that uses
 * 'instanceof' operator to handle all possible pairs of actions.
 * <p/>
 * It is not very Object-Oriented solution, but Java lacks multiple dispatching,
 * that can be very useful in this problem.
 */
@ThreadSafe
@Singleton
public class SimpleActionTransformer implements ActionTransformer {

    @Override
    public ActionPair transform(Action first, Action second) {
        if (first instanceof InsertAction && second instanceof InsertAction) {
            return transformInsertAgainstInsert((InsertAction) first, (InsertAction) second);
        }
        if (first instanceof DeleteAction && second instanceof DeleteAction) {
            return transformDeleteAgainstDelete((DeleteAction) first, (DeleteAction) second);
        }
        if (first instanceof InsertAction && second instanceof DeleteAction) {
            return transformInsertAgainstDelete((InsertAction) first, (DeleteAction) second);
        }
        if (first instanceof DeleteAction && second instanceof InsertAction) {
            ActionPair transformed = transformInsertAgainstDelete((InsertAction) second, (DeleteAction) first);
            return transformed.swap();
        }
        if (first instanceof NoOpAction) {
            return transformAgainstNoOp((NoOpAction) first, second);
        }
        if (second instanceof NoOpAction) {
            return transformNoOpAgainstAnything(first, (NoOpAction) second);
        }
        throw new RuntimeException("Not implemented yet!");
    }

    private ActionPair transformInsertAgainstInsert(InsertAction first, InsertAction second) {
        if (first.getIndex() <= second.getIndex()) {
            Action firstTransformed = insert(first.getValueToInsert()).toPosition(first.getIndex());
            Action secondTransformed = insert(second.getValueToInsert()).toPosition(second.getIndex() + 1);
            return new ActionPair(firstTransformed, secondTransformed);
        } else {
            Action firstTransformed = insert(first.getValueToInsert()).toPosition(first.getIndex() + 1);
            Action secondTransformed = insert(second.getValueToInsert()).toPosition(second.getIndex());
            return new ActionPair(firstTransformed, secondTransformed);
        }
    }

    private ActionPair transformDeleteAgainstDelete(DeleteAction first, DeleteAction second) {
        if (first.getIndex() == second.getIndex()) {
            return new ActionPair(new NoOpAction(), new NoOpAction());
        }
        if (first.getIndex() < second.getIndex()) {
            Action firstTransformed = delete(first.getValueToDelete()).startFrom(first.getIndex());
            Action secondTransformed = delete(second.getValueToDelete()).startFrom(second.getIndex() - 1);
            return new ActionPair(firstTransformed, secondTransformed);
        } else {
            Action firstTransformed = delete(first.getValueToDelete()).startFrom(first.getIndex() - 1);
            Action secondTransformed = delete(second.getValueToDelete()).startFrom(second.getIndex());
            return new ActionPair(firstTransformed, secondTransformed);
        }
    }

    private ActionPair transformInsertAgainstDelete(InsertAction first, DeleteAction second) {
        if (first.getIndex() <= second.getIndex()) {
            Action firstTransformed = insert(first.getValueToInsert()).toPosition(first.getIndex());
            Action secondTransformed = delete(second.getValueToDelete()).startFrom(second.getIndex() + 1);
            return new ActionPair(firstTransformed, secondTransformed);
        } else {
            Action firstTransformed = insert(first.getValueToInsert()).toPosition(first.getIndex() - 1);
            Action secondTransformed = delete(second.getValueToDelete()).startFrom(second.getIndex());
            return new ActionPair(firstTransformed, secondTransformed);
        }
    }

    private ActionPair transformAgainstNoOp(NoOpAction first, Action second) {
        return new ActionPair(first, second);
    }

    private ActionPair transformNoOpAgainstAnything(Action first, NoOpAction second) {
        return new ActionPair(first, second);
    }
}
