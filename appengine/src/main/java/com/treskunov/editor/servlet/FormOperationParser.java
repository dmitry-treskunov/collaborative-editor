package com.treskunov.editor.servlet;

import com.google.inject.Singleton;
import com.treskunov.editor.Action;
import com.treskunov.editor.Operation;
import com.treskunov.editor.servlet.exception.UnrecognizableOperationException;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import static com.treskunov.editor.operation.ActingOperation.OperationBuilder.anOperationBy;
import static com.treskunov.editor.operation.action.DeleteAction.DeleteActionBuilder.delete;
import static com.treskunov.editor.operation.action.InsertAction.InsertActionBuilder.insert;

/**
 * Simple implementation of {@link OperationParser} for clients that send operation attributes
 * in request parameters (using application/x-www-form-urlencoded mime type)
 * <p/>
 * This is an example of supported requests:
 * <pre>
 *         op="insert"&
 *         position="2"&
 *         value="A"&
 *         initiator="Alex"&
 *         version="10
 * </pre>
 */
@Singleton
@ThreadSafe
public class FormOperationParser implements OperationParser {

    private static final Logger logger = LoggerFactory.getLogger(FormOperationParser.class);

    @Override
    public Operation fromRequest(HttpServletRequest request) {
        String initiator = request.getParameter("initiator");
        int version = Integer.parseInt(request.getParameter("version"));
        Action action = parseAction(request);
        return anOperationBy(initiator).onDocumentVersion(version).withAction(action);
    }

    private Action parseAction(HttpServletRequest request) {
        String operationType = request.getParameter("op");
        switch (operationType) {
            case "insert":
                return parseInsertAction(request);
            case "delete":
                return parseDeleteAction(request);
        }
        logger.info("Invalid operation '{}' received", operationType);
        throw new UnrecognizableOperationException(String.format("Operation %s is not supported", operationType));
    }

    private Action parseInsertAction(HttpServletRequest request) {
        String value = request.getParameter("value");
        int index = Integer.valueOf(request.getParameter("position"));
        return insert(value).toPosition(index);
    }

    private Action parseDeleteAction(HttpServletRequest request) {
        String value = request.getParameter("value");
        int index = Integer.valueOf(request.getParameter("position"));
        return delete(value).startFrom(index);
    }

}
