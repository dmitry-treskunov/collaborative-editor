package com.treskunov.editor.operation.action;

import com.treskunov.editor.DocumentContent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.treskunov.editor.operation.action.DeleteAction.DeleteActionBuilder.delete;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DeleteActionTest {

    @Mock
    private DocumentContent content;

    @Test
    public void shouldDeleteTextFromDocument() throws Exception {
        DeleteAction action = delete("Hello").startFrom(10);

        action.applyTo(content);

        verify(content).delete(10, 5);
    }
}
