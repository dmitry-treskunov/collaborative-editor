package com.treskunov.editor.servlet;

import com.treskunov.editor.CollaborativeDocument;
import com.treskunov.editor.CollaborativeDocumentProvider;
import com.treskunov.editor.Operation;
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
import static com.treskunov.editor.matcher.ResponseStatusMatcher.hasStatus;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UpdateOperationServletTest {

    @Mock
    private OperationParser operationParser;
    @Mock
    private CollaborativeDocumentProvider documentProvider;

    @InjectMocks
    private UpdateOperationServlet servlet;

    private CollaborativeDocument existingDocument;
    private String existingDocumentId;

    @Before
    public void setUp() throws Exception {
        existingDocument = mock(CollaborativeDocument.class);
        when(documentProvider.getById(existingDocumentId)).thenReturn(existingDocument);
    }

    @Test
    public void shouldGiveParsedOperationAndDocumentIdToExecutor() throws Exception {
        HttpServletRequest request = aRequest().withParameter("documentId", existingDocumentId).build();
        Operation parsedOperation = initOperationParserAnswerFor(request);

        servlet.doPost(request, new MockHttpServletResponse());

        verify(existingDocument).apply(parsedOperation);
    }

    @Test
    public void shouldReturnNoContentOnAppropriateOperation() throws Exception {
        HttpServletRequest request = aRequest().withParameter("documentId", existingDocumentId).build();
        initOperationParserAnswerFor(request);

        MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doPost(request, response);

        assertThat(response, hasStatus(HttpServletResponse.SC_NO_CONTENT));
    }

    private Operation initOperationParserAnswerFor(HttpServletRequest request) {
        Operation parsedOperation = mock(Operation.class);
        when(operationParser.fromRequest(request)).thenReturn(parsedOperation);
        return parsedOperation;
    }
}
