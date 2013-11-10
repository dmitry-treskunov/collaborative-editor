package com.treskunov.editor.operation.action;

import com.treskunov.editor.DocumentContent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.treskunov.editor.operation.action.InsertAction.InsertActionBuilder.insert;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InsertActionTest {

    @Mock
    private DocumentContent content;

    @Test
    public void shouldInsertValueToDocument() throws Exception {
        InsertAction action = insert("Hello").toPosition(10);

        action.applyTo(content);

        verify(content).insert(10, "Hello");
    }
}
