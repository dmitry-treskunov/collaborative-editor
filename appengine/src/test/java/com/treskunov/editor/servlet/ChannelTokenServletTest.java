package com.treskunov.editor.servlet;

import com.treskunov.editor.channel.ChannelApiCollaborator;
import com.treskunov.editor.channel.ChannelApiCollaboratorsProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;

import static com.treskunov.editor.matcher.ResponseContentTypeMatcher.hasContentType;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChannelTokenServletTest {

    @Mock
    private ChannelApiCollaboratorsProvider collaboratorsProvider;
    @InjectMocks
    private ChannelTokenServlet channelTokenServlet;

    private ChannelApiCollaborator existingCollaborator;
    private String existingCollaboratorId;

    @Before
    public void setUp() throws Exception {
        existingCollaboratorId = "clientId";
        existingCollaborator = mock(ChannelApiCollaborator.class);
        when(collaboratorsProvider.getByClientId(existingCollaboratorId)).thenReturn(existingCollaborator);
    }

    @Test
    public void shouldReturnChannelTokenInResponseBody() throws Exception {
        HttpServletRequest request = createRequestWithClientId(existingCollaboratorId);
        when(existingCollaborator.initChannel()).thenReturn("token#1");

        MockHttpServletResponse response = new MockHttpServletResponse();
        channelTokenServlet.doGet(request, response);

        assertThat(response.getContentAsString(), is("token#1"));
        assertThat(response, hasContentType(ContentType.TEXT_PLAIN));
        //TODO probably, it will be better to create Hamcrest matcher here
    }

    private HttpServletRequest createRequestWithClientId(String clientId) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("clientId", clientId);
        return request;
    }
}
