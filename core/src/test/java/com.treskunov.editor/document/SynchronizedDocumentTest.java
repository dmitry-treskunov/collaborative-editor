package com.treskunov.editor.document;

import com.treskunov.editor.Collaborator;
import com.treskunov.editor.DocumentContent;
import com.treskunov.editor.Operation;
import com.treskunov.editor.operation.OperationRebaser;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class SynchronizedDocumentTest {

    private Collaborator firstCollaborator = mock(Collaborator.class);
    private Collaborator secondCollaborator = mock(Collaborator.class);
    private DocumentContent documentContent;
    private SynchronizedDocument document;
    private String documentTitle = "Hello World Document";
    private String documentId = "id1";
    private OperationRebaser operationRebaser;

    @Before
    public void setUp() throws Exception {
        documentContent = mock(DocumentContent.class);
        operationRebaser = mock(OperationRebaser.class);
        document = new SynchronizedDocument(documentId, documentTitle, documentContent, operationRebaser);
    }

    @Test
    public void shouldInitializeNewDocumentWithZeroVersion() throws Exception {
        assertThat(document.getVersion(), is(0));
    }

    @Test
    public void shouldSaveTitlePassedInConstructor() throws Exception {
        assertThat(document.getTitle(), is(documentTitle));
    }

    @Test
    public void shouldSaveIdPassedInConstructor() throws Exception {
        assertThat(document.getId(), is(documentId));
    }

    @Test
    public void shouldIncrementVersionAfterOperationApplication() throws Exception {
        Operation operation = createOperationWithVersion(0);

        document.apply(operation);

        assertThat(document.getVersion(), is(1));
    }

    @Test
    public void shouldIncrementSnapshotVersionAfterOperationApplication() throws Exception {
        Operation operation = createOperationWithVersion(0);

        document.apply(operation);

        assertThat(document.getSnapshot().getVersion(), is(1));
    }

    @Test
    public void shouldReturnContentInDocumentSnapshot() throws Exception {
        when(documentContent.asText()).thenReturn("Hello world");

        assertThat(document.getSnapshot().getText(), is("Hello world"));
    }

    @Test
    public void shouldReturnTitleInDocumentSnapshot() throws Exception {
        assertThat(document.getSnapshot().getTitle(), is(documentTitle));
    }

    @Test
    public void shouldApplyOperationWithoutTransformationWhenItsVersionMatchesDocumentVersion() throws Exception {
        Operation operation = createOperationWithVersion(0);

        document.apply(operation);

        verify(operation).applyTo(documentContent);
    }

    @Test
    public void shouldRebaseOperationAgainstPreviousAppliedOperationWhenItsVersionIsOneMoreThanDocumentVersion() throws Exception {
        Operation previousOperation = createOperationWithVersion(0);
        applyOperations(previousOperation);
        Operation currentOperation = createOperationWithVersion(0);
        Operation currentRebased = setUpRebaserAnswerFor(currentOperation, previousOperation);

        document.apply(currentOperation);

        verify(currentRebased).applyTo(documentContent);
    }

    @Test
    public void shouldRebaseOperationAgainstAllAppliedOperationsSinceItsVersion() throws Exception {
        Operation firstAppliedOperation = createOperationWithVersion(0);
        Operation secondAppliedOperation = createOperationWithVersion(1);
        applyOperations(firstAppliedOperation, secondAppliedOperation);
        Operation currentOperation = createOperationWithVersion(0);
        Operation currentRebased = setUpRebaserAnswerFor(currentOperation, firstAppliedOperation, secondAppliedOperation);

        document.apply(currentOperation);

        verify(currentRebased).applyTo(documentContent);
    }

    @Test
    public void shouldNotifyCollaboratorsWithAppliedOperationWhenOperationIsNotRebased() throws Exception {
        registerCollaborators(firstCollaborator, secondCollaborator);

        Operation operation = createOperationWithVersion(0);
        document.apply(operation);

        verify(firstCollaborator).onOperationApplied(operation);
        verify(secondCollaborator).onOperationApplied(operation);
    }

    @Test
    public void shouldNotifyCollaboratorsWithRebasedOperationWhenOperationIsRebased() throws Exception {
        registerCollaborators(firstCollaborator, secondCollaborator);
        Operation previousOperation = createOperationWithVersion(0);
        applyOperations(previousOperation);
        Operation currentOperation = createOperationWithVersion(0);
        Operation currentRebased = setUpRebaserAnswerFor(currentOperation, previousOperation);

        document.apply(currentOperation);

        verify(firstCollaborator).onOperationApplied(currentRebased);
        verify(secondCollaborator).onOperationApplied(currentRebased);
    }

    @Test
    public void shouldNotNotifyUnregisteredCollaborators() throws Exception {
        registerCollaborators(firstCollaborator, secondCollaborator);
        unregisterCollaborators(secondCollaborator);

        Operation operation = createOperationWithVersion(0);
        document.apply(operation);

        verify(firstCollaborator).onOperationApplied(operation);
        verify(secondCollaborator, never()).onOperationApplied(operation);
    }

    private void applyOperations(Operation... operations) {
        for (Operation operation : operations) {
            document.apply(operation);
        }
    }

    private void unregisterCollaborators(Collaborator... collaborators) {
        for (Collaborator collaborator : collaborators) {
            document.unregisterCollaborator(collaborator);
        }
    }

    private void registerCollaborators(Collaborator... collaborators) {
        for (Collaborator collaborator : collaborators) {
            document.registerCollaborator(collaborator);
        }
    }

    private Operation createOperationWithVersion(int version) {
        Operation operation = mock(Operation.class);
        when(operation.getDocumentVersion()).thenReturn(version);
        return operation;
    }

    private Operation setUpRebaserAnswerFor(Operation current, Operation... history) {
        Operation currentRebased = mock(Operation.class);
        when(operationRebaser.rebase(current, asList(history))).thenReturn(currentRebased);
        return currentRebased;
    }
}
