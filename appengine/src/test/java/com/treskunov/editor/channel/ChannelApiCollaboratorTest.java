package com.treskunov.editor.channel;

import com.treskunov.editor.Operation;
import org.junit.Before;
import org.junit.Test;

import static com.treskunov.editor.operation.ActingOperation.OperationBuilder.anOperationBy;
import static com.treskunov.editor.operation.action.DeleteAction.DeleteActionBuilder.delete;
import static com.treskunov.editor.operation.action.InsertAction.InsertActionBuilder.insert;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Some tests in this class are fragile, because they depend on exact matching of JSON.
 * For example, if implementation will change order of fields in Json these tests will fail,
 * that is not correct.
 * <p/>
 * TODO verify JSON matching more freely, probably using some Json deserializer library.
 */
public class ChannelApiCollaboratorTest {

    private ChannelApiCollaborator collaborator;
    private Channel channel;

    @Before
    public void setUp() throws Exception {
        channel = mock(Channel.class);
        collaborator = new ChannelApiCollaborator(channel);
    }

    @Test
    public void shouldSerializeInsertOperation() throws Exception {
        Operation operation = anOperationBy("Alex").onDocumentVersion(1).withAction(
                insert("Hello").toPosition(0));
        collaborator.onOperationApplied(operation);

        String expectedSentMessage = "{\"version\":1,\"initiator\":\"Alex\",\"op\":\"insert\",\"position\":0,\"value\":\"Hello\"}";
        verify(channel).sendMessage(expectedSentMessage);
    }

    @Test
    public void shouldSerializeDeleteOperation() throws Exception {
        Operation operation = anOperationBy("Peter").onDocumentVersion(2).withAction(
                delete("Bye").startFrom(3));
        collaborator.onOperationApplied(operation);

        String expectedSentMessage = "{\"version\":2,\"initiator\":\"Peter\",\"op\":\"delete\",\"position\":3,\"value\":\"Bye\"}";
        verify(channel).sendMessage(expectedSentMessage);
    }

    @Test
    public void shouldSerializeNoOpOperation() throws Exception {
        Operation operation = anOperationBy("Peter").onDocumentVersion(2).withoutAction();
        collaborator.onOperationApplied(operation);

        String expectedSentMessage = "{\"version\":2,\"initiator\":\"Peter\",\"op\":\"noop\"}";
        verify(channel).sendMessage(expectedSentMessage);
    }

    @Test
    public void shouldInitChannelAndReturnChannelToken() throws Exception {
        when(channel.init()).thenReturn("channelToken");

        String channelToken = collaborator.initChannel();

        assertThat(channelToken, is("channelToken"));
    }
}
