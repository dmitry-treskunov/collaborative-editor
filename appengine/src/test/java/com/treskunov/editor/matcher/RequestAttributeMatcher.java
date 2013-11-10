package com.treskunov.editor.matcher;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class RequestAttributeMatcher<T> extends TypeSafeDiagnosingMatcher<HttpServletRequest> {

    private String attributeName;
    private Matcher<T> attributeMatcher;

    public RequestAttributeMatcher(String attributeName, Matcher<T> attributeMatcher) {
        this.attributeName = attributeName;
        this.attributeMatcher = attributeMatcher;
    }

    @Override
    protected boolean matchesSafely(HttpServletRequest request, Description mismatchDescription) {
        Object attribute = request.getAttribute(attributeName);
        if (attribute == null) {
            mismatchDescription.
                    appendText("request containing attributes ").
                    appendText(getAllAttributesNames(request));
            return false;
        }

        if (!attributeMatcher.matches(attribute)) {
            mismatchDescription.
                    appendText("request's attribute '").
                    appendText(attributeName).
                    appendText("' ");
            attributeMatcher.describeMismatch(attribute, mismatchDescription);
            return false;
        }

        return true;
    }

    private String getAllAttributesNames(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        Enumeration names = request.getAttributeNames();
        if (names.hasMoreElements()) {
            appendName(builder, names);
            while (names.hasMoreElements()) {
                appendSeparator(builder);
                appendName(builder, names);
            }
        }
        return builder.toString();
    }

    private StringBuilder appendSeparator(StringBuilder builder) {
        return builder.append(", ");
    }

    private StringBuilder appendName(StringBuilder builder, Enumeration attributeNames) {
        return builder.append("'").append(attributeNames.nextElement()).append("'");
    }

    @Override
    public void describeTo(Description description) {
        description.
                appendText("a request containing attribute ").
                appendDescriptionOf(attributeMatcher).
                appendText(" by name '").appendText(attributeName).appendText("'");
    }

    public static Matcher<HttpServletRequest> hasNamedAttribute(String name, String attribute) {
        return new RequestAttributeMatcher<>(name, CoreMatchers.equalTo(attribute));
    }
}
