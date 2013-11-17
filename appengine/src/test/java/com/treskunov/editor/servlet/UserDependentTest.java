package com.treskunov.editor.servlet;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import org.junit.After;
import org.junit.Before;

/**
 * Google App Engine provides {@link LocalServiceTestHelper} that allows
 * to configure {@link com.google.appengine.api.users.UserService} used in tested classes.
 */
public abstract class UserDependentTest {

    private final String currentUserEmail = "email@host.com";
    private final String currentAuthDomain = "example.com";

    private final LocalServiceTestHelper userServiceHelper =
            new LocalServiceTestHelper(new LocalUserServiceTestConfig())
                    .setEnvIsLoggedIn(true).
                    setEnvEmail(currentUserEmail).
                    setEnvAuthDomain(currentAuthDomain);

    @Before
    public void initCurrentUser() {
        userServiceHelper.setUp();
    }

    @After
    public void clearCurrentUser() {
        userServiceHelper.tearDown();
    }

    protected String getCurrentUserEmail() {
        return currentUserEmail;
    }
}
