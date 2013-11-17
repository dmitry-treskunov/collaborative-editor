package com.treskunov.editor.operation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.treskunov.editor.Action;
import com.treskunov.editor.Operation;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ListIterator;

import static com.treskunov.editor.operation.ActingOperation.OperationBuilder.anOperationBy;

/**
 * {@link OperationRebaser} that iterates through history of operations and
 * apply Operational Transformation to current operation and each operation from the history.
 */
@Singleton
@ThreadSafe
public class TransformingOperationRebaser implements OperationRebaser {

    private static final Logger logger = LoggerFactory.getLogger(TransformingOperationRebaser.class);

    private ActionTransformer actionTransformer;

    @Inject
    public TransformingOperationRebaser(ActionTransformer actionTransformer) {
        this.actionTransformer = actionTransformer;
    }

    @Override
    public Operation rebase(Operation current, List<Operation> history) {
        logger.info("Transform operation {} against history {}", current, history);
        Action transformedAction = transformAction(current, history);
        int rebasedOperationVersion = current.getDocumentVersion() + history.size();
        return anOperationBy(current.getInitiator()).
                onDocumentVersion(rebasedOperationVersion).
                withAction(transformedAction);
    }

    private Action transformAction(Operation current, List<Operation> history) {
        ListIterator<Operation> operationsToTransformAgainst = history.listIterator();
        Action currentAction = current.getAction();
        while (operationsToTransformAgainst.hasNext()) {
            Action historyAction = operationsToTransformAgainst.next().getAction();
            ActionPair transformedPair = actionTransformer.transform(historyAction, currentAction);
            currentAction = transformedPair.getSecond();
        }
        return currentAction;
    }
}
