package com.treskunov.editor.operation;

import com.treskunov.editor.Action;
import com.treskunov.editor.Operation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.treskunov.editor.operation.ActingOperation.OperationBuilder.anOperationBy;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransformingOperationRebaserTest {

    @Mock
    private ActionTransformer actionTransformer;

    @InjectMocks
    private TransformingOperationRebaser rebaser;

    private FakeAction currentAction = new FakeAction("current");
    private Operation currentOperation;
    private int currentOperationVersion = 2;
    private String currentOperationInitiator = "Dmitry";

    @Before
    public void setUp() throws Exception {
        currentOperation = anOperationBy(currentOperationInitiator).
                onDocumentVersion(currentOperationVersion).withAction(currentAction);
    }

    @Test
    public void shouldTransformCurrentActionWhenHistoryContainsOneOperation() throws Exception {
        Operation previousOperation = createOperationWithVersion(currentOperationVersion);
        Action currentActionTransformed = setUpTransformerAnswersChain(currentOperation, previousOperation);

        Operation currentRebased = rebaser.rebase(currentOperation, asList(previousOperation));

        assertThat(currentRebased.getAction(), is(currentActionTransformed));
    }

    @Test
    public void shouldTransformCurrentWhenHistoryContainsSeveralOperations() throws Exception {
        Operation firstOperation = createOperationWithVersion(currentOperationVersion);
        Operation secondOperation = createOperationWithVersion(currentOperationVersion + 1);
        Action currentActionTransformed = setUpTransformerAnswersChain(currentOperation, firstOperation, secondOperation);

        Operation currentRebased = rebaser.rebase(currentOperation, asList(firstOperation, secondOperation));

        assertThat(currentRebased.getAction(), is(currentActionTransformed));
    }

    @Test
    public void shouldUpdateRebasedOperationVersion() {
        Operation firstOperation = createOperationWithVersion(currentOperationVersion);
        Operation secondOperation = createOperationWithVersion(currentOperationVersion + 1);
        setUpTransformerAnswersChain(currentOperation, firstOperation, secondOperation);

        Operation currentRebased = rebaser.rebase(currentOperation, asList(firstOperation, secondOperation));

        assertThat(currentRebased.getDocumentVersion(), is(currentOperationVersion + 2));
    }

    @Test
    public void shouldKeepCurrentOperationInitiator() {
        Operation previousOperation = anOperationBy("Alex").onDocumentVersion(1).withAction(new FakeAction("previous"));
        setUpTransformerAnswersChain(currentOperation, previousOperation);

        Operation currentRebased = rebaser.rebase(currentOperation, asList(previousOperation));

        assertThat(currentRebased.getInitiator(), is(currentOperationInitiator));
    }

    private Action setUpTransformerAnswersChain(Operation current, Operation... history) {
        Action currentAction = current.getAction();
        for (int i = 0; i < history.length; i++) {
            Action historyItem = history[i].getAction();
            FakeAction historyItemTransformed = new FakeAction("HistoryItemTransformed" + i);
            FakeAction currentActionTransformed = new FakeAction("CurrentTransformed" + i);
            ActionPair transformedPair = new ActionPair(historyItemTransformed, currentActionTransformed);
            when(actionTransformer.transform(historyItem, currentAction)).thenReturn(transformedPair);
            currentAction = currentActionTransformed;
        }
        return currentAction;
    }

    private Operation createOperationWithVersion(int version) {
        return anOperationBy("Dmitry").onDocumentVersion(version).withAction(new FakeAction("Operation" + version));
    }
}
