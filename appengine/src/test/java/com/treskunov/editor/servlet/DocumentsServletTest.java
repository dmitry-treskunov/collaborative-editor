package com.treskunov.editor.servlet;

import com.treskunov.editor.CollaborativeDocument;
import com.treskunov.editor.CollaborativeDocumentProvider;
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
import static com.treskunov.editor.matcher.ResponseRedirectMatcher.hasRedirectUri;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DocumentsServletTest extends UserDependentTest {

    @Mock
    private CollaborativeDocumentProvider documentProvider;

    @InjectMocks
    private DocumentsServlet documentsListServlet;

    private CollaborativeDocument firstExistingDocument;
    private CollaborativeDocument secondExistingDocument;

    @Before
    public void initializeExistingDocuments() throws Exception {
        firstExistingDocument = mock(CollaborativeDocument.class);
        secondExistingDocument = mock(CollaborativeDocument.class);
        when(firstExistingDocument.getTitle()).thenReturn("First Document");
        when(firstExistingDocument.getId()).thenReturn("1");
        when(secondExistingDocument.getTitle()).thenReturn("Second Document");
        when(secondExistingDocument.getId()).thenReturn("2");
    }

    @Test
    public void shouldReturnAllDocumentsOnGetRequest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        when(documentProvider.getAllDocuments()).thenReturn(asList(firstExistingDocument, secondExistingDocument));

        documentsListServlet.doGet(request, new MockHttpServletResponse());

        assertThat(request, hasNamedAttribute("documents", hasItems(firstExistingDocument, secondExistingDocument)));
    }

    @Test
    public void shouldPutUserNameToRequestAttributesOnGetRequest() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        when(documentProvider.getAllDocuments()).thenReturn(asList(firstExistingDocument, secondExistingDocument));

        documentsListServlet.doGet(request, new MockHttpServletResponse());

        assertThat(request, hasNamedAttribute("userEmail", getCurrentUserEmail()));
    }

    @Test
    public void shouldCreateNewDocumentOnPostRequest() throws Exception {
        HttpServletRequest request = createPostRequestWithDocumentTitle("Hello world!");
        CollaborativeDocument createdDocument = createDocumentWithId("doc1");
        when(documentProvider.create("Hello world!")).thenReturn(createdDocument);

        documentsListServlet.doPost(request, new MockHttpServletResponse());

        verify(documentProvider).create("Hello world!");
    }

    @Test
    public void shouldRedirectOnCreatedDocumentPage() throws Exception {
        HttpServletRequest request = createPostRequestWithDocumentTitle("Hello world!");
        CollaborativeDocument createdDocument = createDocumentWithId("doc1");
        when(documentProvider.create("Hello world!")).thenReturn(createdDocument);

        MockHttpServletResponse response = new MockHttpServletResponse();
        documentsListServlet.doPost(request, response);

        assertThat(response, hasRedirectUri("/editor?documentId=doc1"));
    }

    private CollaborativeDocument createDocumentWithId(String id) {
        CollaborativeDocument document = mock(CollaborativeDocument.class);
        when(document.getId()).thenReturn(id);
        return document;
    }

    private HttpServletRequest createPostRequestWithDocumentTitle(String title) {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/");
        request.setParameter("title", title);
        return request;
    }
}
