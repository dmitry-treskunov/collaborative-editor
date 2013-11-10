package com.treskunov.editor.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.servlet.http.HttpServletResponse;

public class ResponseContentTypeMatcher extends TypeSafeDiagnosingMatcher<HttpServletResponse> {

    private final String expectedContentType;

    public ResponseContentTypeMatcher(String expectedContentType) {
        this.expectedContentType = expectedContentType;
    }

    @Override
    protected boolean matchesSafely(HttpServletResponse response, Description mismatchDescription) {
        String actualContentType = response.getContentType();
        if (!expectedContentType.equals(actualContentType)) {
            mismatchDescription.
                    appendText("response with Content Type '").
                    appendText(actualContentType).
                    appendText("'");
            return false;
        }
        return true;
    }


    @Override
    public void describeTo(Description description) {
        description.
                appendText("a response with Content Type ' ").
                appendText(expectedContentType).
                appendText("'");
    }

    public static Matcher<HttpServletResponse> hasJsonContentType() {
        return new ResponseContentTypeMatcher("application/json");
    }

    public static Matcher<HttpServletResponse> hasPlainTextContentType() {
        return new ResponseContentTypeMatcher("text/plain");
    }
}
