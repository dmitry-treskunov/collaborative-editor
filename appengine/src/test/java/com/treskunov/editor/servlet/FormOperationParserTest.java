package com.treskunov.editor.servlet;

import com.treskunov.editor.Operation;
import com.treskunov.editor.servlet.exception.UnrecognizableOperationException;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.treskunov.editor.operation.ActingOperation.OperationBuilder.anOperationBy;
import static com.treskunov.editor.operation.action.DeleteAction.DeleteActionBuilder.delete;
import static com.treskunov.editor.operation.action.InsertAction.InsertActionBuilder.insert;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FormOperationParserTest {

    private FormOperationParser parser = new FormOperationParser();

    @Test
    public void shouldParseInsertTextOperation() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("op", "insert");
        request.setParameter("position", "3");
        request.setParameter("value", "Hello");
        request.setParameter("initiator", "Dmitry");
        request.setParameter("version", "2");

        Operation operation = parser.fromRequest(request);

        Operation expectedOperation = anOperationBy("Dmitry").onDocumentVersion(2).withAction(
                insert("Hello").toPosition(3));
        assertThat(operation, is(equalTo(expectedOperation)));
    }

    @Test
    public void shouldParseDeleteTextOperation() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("op", "delete");
        request.setParameter("position", "3");
        request.setParameter("value", "Bye");
        request.setParameter("initiator", "Dmitry");
        request.setParameter("version", "2");

        Operation operation = parser.fromRequest(request);

        Operation expectedOperation = anOperationBy("Dmitry").onDocumentVersion(2).withAction(
                delete("Bye").startFrom(3));
        assertThat(operation, is(equalTo(expectedOperation)));
    }

    @Test(expected = UnrecognizableOperationException.class)
    public void shouldFailToParseUnrecognizableOperation() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("op", "unrecognizable");
        request.setParameter("initiator", "Dmitry");
        request.setParameter("version", "2");

        parser.fromRequest(request);
    }
}
