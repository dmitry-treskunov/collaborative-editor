package com.treskunov.editor.builder;

import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

public final class RequestBuilder {

    private MockHttpServletRequest request;

    private RequestBuilder() {
        request = new MockHttpServletRequest();
    }

    private RequestBuilder(String method, String uri) {
        request = new MockHttpServletRequest(method, uri);
    }

    public static RequestBuilder aRequest() {
        return new RequestBuilder();
    }

    public static RequestBuilder aRequest(String method, String uri) {
        return new RequestBuilder(method, uri);
    }

    public RequestBuilder withParameter(String name, String value) {
        request.setParameter(name, value);
        return this;
    }

    public HttpServletRequest build() {
        return request;
    }
}
