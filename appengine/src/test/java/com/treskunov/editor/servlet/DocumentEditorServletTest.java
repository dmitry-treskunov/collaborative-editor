package com.treskunov.editor.servlet;

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

import static com.treskunov.editor.matcher.RequestAttributeMatcher.hasNamedAttribute;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DocumentEditorServletTest {

    @Mock
    private ChannelApiCollaboratorsProvider mockCollaboratorsProvider;
    @InjectMocks
    private DocumentEditorServlet documentEditorServlet;

    @Before
    public void setUp() throws Exception {
        when(mockCollaboratorsProvider.create()).thenReturn("id1");
    }

    @Test
    public void shouldCreateCollaboratorAndPutItIdToRequestAttributes() throws Exception {
        HttpServletRequest request = createRequestWithDocumentId("doc#1");
        when(mockCollaboratorsProvider.create()).thenReturn("collaborator#2");

        documentEditorServlet.doGet(request, new MockHttpServletResponse());

        assertThat(request, hasNamedAttribute("clientId", "collaborator#2"));
    }

    @Test
    public void shouldPutDocumentIdToRequestAttributes() throws Exception {
        HttpServletRequest request = createRequestWithDocumentId("doc#1");

        documentEditorServlet.doGet(request, new MockHttpServletResponse());

        assertThat(request, hasNamedAttribute("documentId", "doc#1"));
    }

    private HttpServletRequest createRequestWithDocumentId(String documentId) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("documentId", documentId);
        return request;
    }
}
