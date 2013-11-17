package com.treskunov.editor.servlet;

import com.treskunov.editor.CollaborativeDocument;
import com.treskunov.editor.CollaborativeDocumentProvider;
import com.treskunov.editor.DocumentSnapshot;
import com.treskunov.editor.channel.ChannelApiCollaborator;
import com.treskunov.editor.channel.ChannelApiCollaboratorsProvider;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.treskunov.editor.builder.RequestBuilder.aRequest;
import static com.treskunov.editor.matcher.ResponseContentTypeMatcher.hasContentType;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GetDocumentServletTest {

    private String existingDocumentId = "doc#1";
    private DocumentSnapshot existingDocumentSnapshot = new DocumentSnapshot("Hello", "HelloWorld!", 1);
    private CollaborativeDocument existingDocument;

    @InjectMocks
    private GetDocumentServlet servlet;

    @Mock
    private CollaborativeDocumentProvider documentProvider;

    @Mock
    private ChannelApiCollaboratorsProvider collaboratorsProvider;

    @Before
    public void initExistingDocument() throws Exception {
        existingDocument = mock(CollaborativeDocument.class);
        when(existingDocument.getSnapshot()).thenReturn(existingDocumentSnapshot);
        when(documentProvider.getById(existingDocumentId)).thenReturn(existingDocument);
    }

    @Test
    public void shouldRespondWithJsonContent() throws Exception {
        HttpServletRequest request = aRequest().withParameter("documentId", existingDocumentId).build();

        HttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);

        assertThat(response, hasContentType(ContentType.APPLICATION_JSON));
    }

    @Test
    public void shouldPutDocumentTitleInResponse() throws Exception {
        HttpServletRequest request = aRequest().withParameter("documentId", existingDocumentId).build();

        MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);

        JSONObject responseContent = new JSONObject(response.getContentAsString());
        assertThat(responseContent.getString("documentTitle"), is(existingDocumentSnapshot.getTitle()));
    }

    @Test
    public void shouldPutDocumentVersionInResponse() throws Exception {
        HttpServletRequest request = aRequest().withParameter("documentId", existingDocumentId).build();

        MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);

        JSONObject responseContent = new JSONObject(response.getContentAsString());
        assertThat(responseContent.getInt("documentVersion"), is(existingDocumentSnapshot.getVersion()));
    }

    @Test
    public void shouldPutDocumentContentInResponse() throws Exception {
        HttpServletRequest request = aRequest().withParameter("documentId", existingDocumentId).build();

        MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(request, response);

        JSONObject responseContent = new JSONObject(response.getContentAsString());
        assertThat(responseContent.getString("documentText"), is(existingDocumentSnapshot.getText()));
    }

    @Test
    public void shouldRegisterClientAsDocumentCollaborator() throws Exception {
        HttpServletRequest request = aRequest().
                withParameter("documentId", existingDocumentId).
                withParameter("clientId", "client#1").
                build();
        ChannelApiCollaborator collaborator = mock(ChannelApiCollaborator.class);
        when(collaboratorsProvider.getByClientId("client#1")).thenReturn(collaborator);

        servlet.doGet(request, new MockHttpServletResponse());

        verify(existingDocument).registerCollaborator(collaborator);
    }

}
