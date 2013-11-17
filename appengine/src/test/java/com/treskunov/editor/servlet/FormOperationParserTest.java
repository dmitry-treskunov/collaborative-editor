package com.treskunov.editor.servlet;

import com.treskunov.editor.Operation;
import com.treskunov.editor.servlet.exception.UnrecognizableOperationException;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static com.treskunov.editor.builder.RequestBuilder.aRequest;
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
        HttpServletRequest request = aRequest().
                withParameter("op", "insert").
                withParameter("position", "3").
                withParameter("value", "Hello").
                withParameter("initiator", "Dmitry").
                withParameter("version", "2").build();

        Operation operation = parser.fromRequest(request);

        Operation expectedOperation = anOperationBy("Dmitry").onDocumentVersion(2).withAction(
                insert("Hello").toPosition(3));
        assertThat(operation, is(equalTo(expectedOperation)));
    }

    @Test
    public void shouldParseDeleteTextOperation() throws Exception {
        HttpServletRequest request = aRequest().
                withParameter("op", "delete").
                withParameter("position", "3").
                withParameter("value", "Bye").
                withParameter("initiator", "Dmitry").
                withParameter("version", "2").build();

        Operation operation = parser.fromRequest(request);

        Operation expectedOperation = anOperationBy("Dmitry").onDocumentVersion(2).withAction(
                delete("Bye").startFrom(3));
        assertThat(operation, is(equalTo(expectedOperation)));
    }

    @Test(expected = UnrecognizableOperationException.class)
    public void shouldFailToParseUnrecognizableOperation() throws Exception {
        HttpServletRequest request = aRequest().
                withParameter("op", "unrecognizable").
                withParameter("initiator", "Dmitry").
                withParameter("version", "2").build();

        parser.fromRequest(request);
    }
}
