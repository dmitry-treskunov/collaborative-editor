package com.treskunov.editor.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * Unfortunately Servlet 2.5 {@link HttpServletResponse} doesn't contain
 * any method to fetch response status. So we have to use {@link MockHttpServletResponse} here.
 * <p/>
 * NOTE: in Servlet 3.0 there is such method.
 * TODO does App Engine supports servlet 3.0?
 */
public class ResponseStatusMatcher extends TypeSafeDiagnosingMatcher<MockHttpServletResponse> {

    private final int expectedStatus;

    public ResponseStatusMatcher(int expectedStatus) {
        this.expectedStatus = expectedStatus;
    }

    @Override
    protected boolean matchesSafely(MockHttpServletResponse response, Description mismatchDescription) {
        int actualStatus = response.getStatus();
        if (actualStatus != expectedStatus) {
            mismatchDescription.
                    appendText("response with status ").
                    appendText(String.valueOf(actualStatus));
            return false;
        }
        return true;
    }


    @Override
    public void describeTo(Description description) {
        description.
                appendText("response with status ").
                appendText(String.valueOf(expectedStatus));
    }

    public static Matcher<MockHttpServletResponse> hasStatus(int statusCode) {
        return new ResponseStatusMatcher(statusCode);
    }
}
