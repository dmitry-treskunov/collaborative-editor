package com.treskunov.editor.servlet;

import com.treskunov.editor.Operation;

import javax.servlet.http.HttpServletRequest;

/**
 * Fetches {@link Operation} from client request.
 */
public interface OperationParser {

    /**
     * @throws UnsupportedOperationException if request contains operation that can not be
     *                                       mapped to any {@link Operation}
     */
    Operation fromRequest(HttpServletRequest request);
}
