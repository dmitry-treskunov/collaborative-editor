package com.treskunov.editor.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.springframework.mock.web.MockHttpServletResponse;

public class ResponseRedirectMatcher extends TypeSafeDiagnosingMatcher<MockHttpServletResponse> {

    private final String expectedRedirect;

    public ResponseRedirectMatcher(String expectedRedirect) {
        this.expectedRedirect = expectedRedirect;
    }

    @Override
    protected boolean matchesSafely(MockHttpServletResponse response, Description mismatchDescription) {
        String actualRedirect = response.getRedirectedUrl();
        if (!expectedRedirect.equals(actualRedirect)) {
            mismatchDescription.
                    appendText("response with redirect uri ").
                    appendText(actualRedirect);
            return false;
        }
        return true;
    }


    @Override
    public void describeTo(Description description) {
        description.
                appendText("response with redirect uri ").
                appendText(expectedRedirect);
    }

    public static Matcher<MockHttpServletResponse> hasRedirectUri(String expectedRedirect) {
        return new ResponseRedirectMatcher(expectedRedirect);
    }
}
