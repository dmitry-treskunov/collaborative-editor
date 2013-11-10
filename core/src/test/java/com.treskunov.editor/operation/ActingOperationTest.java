package com.treskunov.editor.operation;

import com.treskunov.editor.Action;
import com.treskunov.editor.DocumentContent;
import com.treskunov.editor.Operation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.treskunov.editor.operation.ActingOperation.OperationBuilder.anOperationBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ActingOperationTest {

    @Mock
    private Action action;

    @Test
    public void shouldApplyActionToDocument() throws Exception {
        Operation operation = anOperationBy("Alex").onDocumentVersion(10).withAction(action);

        DocumentContent documentContent = createDocument();
        operation.applyTo(documentContent);

        verify(action).applyTo(documentContent);
    }

    private DocumentContent createDocument() {
        return mock(DocumentContent.class);
    }
}
