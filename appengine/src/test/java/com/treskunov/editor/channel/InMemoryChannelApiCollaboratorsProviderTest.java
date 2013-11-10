package com.treskunov.editor.channel;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class InMemoryChannelApiCollaboratorsProviderTest {

    private InMemoryChannelApiCollaboratorsProvider provider = new InMemoryChannelApiCollaboratorsProvider();

    @Test
    public void shouldInitCollaboratorsByDifferentIds() throws Exception {
        String firstCollaboratorId = provider.create();

        String secondCollaboratorId = provider.create();

        assertThat(secondCollaboratorId, is(not(equalTo(firstCollaboratorId))));
    }

    @Test
    public void shouldRememberCreatedCollaborator() throws Exception {
        String createdCollaboratorId = provider.create();

        ChannelApiCollaborator foundCollaborator = provider.getByClientId(createdCollaboratorId);

        assertThat(foundCollaborator, is(notNullValue()));
    }
}
