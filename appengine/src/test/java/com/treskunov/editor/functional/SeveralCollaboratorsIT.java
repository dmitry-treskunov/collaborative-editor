package com.treskunov.editor.functional;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * TODO move functional tests to another module/directory
 */
@Ignore
public class SeveralCollaboratorsIT {

    private Collaborator firstCollaborator;
    private Collaborator secondCollaborator;

    /**
     * TODO use Chrome driver
     */
    @Before
    public void setUpCollaborators() throws Exception {
        firstCollaborator = new Collaborator();
        secondCollaborator = new Collaborator();
    }

    @After
    public void tearDownCollaborators() throws Exception {
        firstCollaborator.quit();
        secondCollaborator.quit();
    }

    /**
     * '' -> 'A'
     */
    @Test
    public void shouldPropagateFirstLetterAdditionFromFirstCollaboratorToSecondOne() throws Exception {
        firstCollaborator.typeText("A");

        secondCollaborator.ensureTextBecame("A");
    }
}
