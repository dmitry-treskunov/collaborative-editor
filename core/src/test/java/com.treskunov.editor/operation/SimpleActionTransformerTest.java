package com.treskunov.editor.operation;

import com.treskunov.editor.Action;
import com.treskunov.editor.document.StringDocumentContent;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static com.treskunov.editor.operation.action.DeleteAction.DeleteActionBuilder.delete;
import static com.treskunov.editor.operation.action.InsertAction.InsertActionBuilder.insert;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Operational transformation should maintain the next inveriant:
 * <pre>
 *     transform(a,b) = (a',b') where a(b'(document)) == b(a'(document))
 * </pre>
 * <p/>
 * TODO split this test class to several classes
 */
public class SimpleActionTransformerTest {

    private ActionTransformer transformer = new SimpleActionTransformer();

    private StringDocumentContent firstCollaboratorDocument = new StringDocumentContent("Hello world");
    private StringDocumentContent secondCollaboratorDocument = new StringDocumentContent("Hello world");
    private Action firstCollaboratorAction;
    private Action secondCollaboratorAction;

    // Insert vs Insert
    @Test
    public void shouldTransformInsertAgainstInsertAction_FirstIndexLessThenSecond() throws Exception {
        firstCollaboratorAction = insert(",").toPosition(5);
        secondCollaboratorAction = insert("!").toPosition(11);

        ActionPair transformed = transformer.transform(firstCollaboratorAction, secondCollaboratorAction);
        applyActions(transformed);

        assertBothDocumentsAre(new StringDocumentContent("Hello, world!"));
    }

    @Test
    public void shouldTransformInsertAgainstInsertActions_SecondIndexLessThenFirst() throws Exception {
        firstCollaboratorAction = insert("!").toPosition(11);
        secondCollaboratorAction = insert(",").toPosition(5);

        ActionPair transformed = transformer.transform(firstCollaboratorAction, secondCollaboratorAction);
        applyActions(transformed);

        assertBothDocumentsAre(new StringDocumentContent("Hello, world!"));
    }

    @Test
    public void shouldTransformInsertAgainstInsertActions_SameIndexes() throws Exception {
        firstCollaboratorAction = insert("s").toPosition(11);
        secondCollaboratorAction = insert("!").toPosition(11);

        ActionPair transformed = transformer.transform(firstCollaboratorAction, secondCollaboratorAction);
        applyActions(transformed);

        assertBothDocumentsAre(new StringDocumentContent("Hello worlds!"));
    }

    //Delete vs delete
    @Test
    public void shouldTransformDeleteAgainstDeleteAction_FirstIndexLessThenSecond() throws Exception {
        firstCollaboratorAction = delete("o").startFrom(4);
        secondCollaboratorAction = delete("d").startFrom(10);

        ActionPair transformed = transformer.transform(firstCollaboratorAction, secondCollaboratorAction);
        applyActions(transformed);

        assertBothDocumentsAre(new StringDocumentContent("Hell worl"));
    }

    @Test
    public void shouldTransformDeleteAgainstDeleteActions_SecondIndexLessThenFirst() throws Exception {
        firstCollaboratorAction = delete("H").startFrom(0);
        secondCollaboratorAction = delete("w").startFrom(6);

        ActionPair transformed = transformer.transform(firstCollaboratorAction, secondCollaboratorAction);
        applyActions(transformed);

        assertBothDocumentsAre(new StringDocumentContent("ello orld"));
    }

    @Test
    public void shouldTransformDeleteAgainstDeleteActions_NeighborsIndexes() throws Exception {
        firstCollaboratorAction = delete("l").startFrom(2);
        secondCollaboratorAction = delete("l").startFrom(3);

        ActionPair transformed = transformer.transform(firstCollaboratorAction, secondCollaboratorAction);
        applyActions(transformed);

        assertBothDocumentsAre(new StringDocumentContent("Heo world"));
    }

    @Test
    public void shouldTransformDeleteAgainstDeleteActions_SameIndexes() throws Exception {
        firstCollaboratorAction = delete("l").startFrom(2);
        secondCollaboratorAction = delete("l").startFrom(2);

        ActionPair transformed = transformer.transform(firstCollaboratorAction, secondCollaboratorAction);
        applyActions(transformed);

        assertBothDocumentsAre(new StringDocumentContent("Helo world"));
    }

    //Insert vs delete
    @Test
    public void shouldTransformInsertAgainstDeleteAction_FirstIndexLessThenSecond() throws Exception {
        firstCollaboratorAction = insert(",").toPosition(5);
        secondCollaboratorAction = delete("d").startFrom(10);

        ActionPair transformed = transformer.transform(firstCollaboratorAction, secondCollaboratorAction);
        applyActions(transformed);

        assertBothDocumentsAre(new StringDocumentContent("Hello, worl"));
    }

    @Test
    public void shouldTransformInsertAgainstDeleteActions_SecondIndexLessThenFirst() throws Exception {
        firstCollaboratorAction = insert(",").toPosition(5);
        secondCollaboratorAction = delete("H").startFrom(0);

        ActionPair transformed = transformer.transform(firstCollaboratorAction, secondCollaboratorAction);
        applyActions(transformed);

        assertBothDocumentsAre(new StringDocumentContent("ello, world"));
    }

    @Test
    public void shouldTransformInsertAgainstDeleteActions_SameIndexes() throws Exception {
        firstCollaboratorAction = insert("e").toPosition(1);
        secondCollaboratorAction = delete("e").startFrom(1);

        ActionPair transformed = transformer.transform(firstCollaboratorAction, secondCollaboratorAction);
        applyActions(transformed);

        assertBothDocumentsAre(new StringDocumentContent("Hello world"));
    }

    //Delete vs insert
    @Test
    public void shouldTransformDeleteAgainstInsertAction_FirstIndexLessThenSecond() throws Exception {
        firstCollaboratorAction = delete("o").startFrom(4);
        secondCollaboratorAction = insert("o").toPosition(9);

        ActionPair transformed = transformer.transform(firstCollaboratorAction, secondCollaboratorAction);
        applyActions(transformed);

        assertBothDocumentsAre(new StringDocumentContent("Hell worold"));
    }

    @Test
    public void shouldTransformDeleteAgainstInsertActions_SecondIndexLessThenFirst() throws Exception {
        firstCollaboratorAction = delete("l").startFrom(9);
        secondCollaboratorAction = insert("o").toPosition(3);

        ActionPair transformed = transformer.transform(firstCollaboratorAction, secondCollaboratorAction);
        applyActions(transformed);

        assertBothDocumentsAre(new StringDocumentContent("Helolo word"));
    }

    @Test
    public void shouldTransformDeleteAgainstInsertActions_SameIndexes() throws Exception {
        firstCollaboratorAction = delete(" ").startFrom(5);
        secondCollaboratorAction = insert(",").toPosition(5);

        ActionPair transformed = transformer.transform(firstCollaboratorAction, secondCollaboratorAction);
        applyActions(transformed);

        assertBothDocumentsAre(new StringDocumentContent("Hello,world"));
    }

    private void applyActions(ActionPair transformed) {
        applyActions(firstCollaboratorDocument, firstCollaboratorAction, transformed.getSecond());
        applyActions(secondCollaboratorDocument, secondCollaboratorAction, transformed.getFirst());
    }

    private void applyActions(StringDocumentContent document, Action... actions) {
        for (Action action : actions) {
            action.applyTo(document);
        }
    }

    private void assertBothDocumentsAre(StringDocumentContent expectedDocument) {
        assertThat("First collaborator document is incorrect", firstCollaboratorDocument, is(CoreMatchers.equalTo(expectedDocument)));
        assertThat("Second collaborator document is incorrect", secondCollaboratorDocument, is(CoreMatchers.equalTo(expectedDocument)));
    }
}
