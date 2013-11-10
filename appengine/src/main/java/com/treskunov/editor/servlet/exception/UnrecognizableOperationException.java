package com.treskunov.editor.servlet.exception;

/**
 * Exception thrown when client sent request to update Document
 * that can't be parsed to {@link com.treskunov.editor.Operation}.
 *
 * TODO this exception should be mapped to BAD_REQUEST response
 */
public class UnrecognizableOperationException extends RuntimeException {

    public UnrecognizableOperationException() {
    }

    public UnrecognizableOperationException(String message) {
        super(message);
    }
}
