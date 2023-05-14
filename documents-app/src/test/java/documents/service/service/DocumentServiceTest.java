package documents.service.service;

import documents.dao.DocumentDao;
import documents.dao.DocumentTypeDao;
import documents.dto.files.documents.ConcreteDocumentDto;
import documents.dto.files.documents.DocumentDto;
import documents.dto.user.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;



import java.util.List;

import static org.junit.Assert.*;

public class DocumentServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private AccessService accessService;

    @Mock
    private DocumentDao documentDao;

    @Mock
    private DocumentTypeDao documentTypeDao;

    @InjectMocks
    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllDocuments() {
        // Mock the DocumentDao to return a page of documents
        Page<DocumentDto> mockPageOfDocuments = new PageImpl<>(List.of(DocumentDto.builder().build(), DocumentDto.builder().build()));
        Mockito.when(documentDao.getAllDocuments(Mockito.any(Pageable.class), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(mockPageOfDocuments);
        // Call the getAllDocuments method with arbitrary input parameters
        Page<DocumentDto> actualPageOfDocuments = documentService.getAllDocuments(0, 10, "name", "type");
        // Verify that the Page<DocumentDto> returned by the getAllDocuments method matches the mock page
        assertEquals(mockPageOfDocuments, actualPageOfDocuments);
    }

    @Test
    public void testGetDocumentById() {
        // Mock the AccessService to return true for chekRAccess method
        Mockito.when(accessService.chekRAccess(Mockito.anyLong())).thenReturn(true);

        // Mock the DocumentDao to return a document
        DocumentDto mockDocument = DocumentDto.builder().build();
        Mockito.when(documentDao.getDocumentById(Mockito.anyLong())).thenReturn(mockDocument);

        // Call the getDocumentById method with an arbitrary id
        DocumentDto actualDocument = documentService.getDocumentById(1L);

        // Verify that the DocumentDto returned by the getDocumentById method matches the mock document
        assertEquals(mockDocument, actualDocument);
    }

    @Test
    public void testGetDocumentById_withoutAccess() {
        Long id = 1L;

        when(accessService.chekRAccess(id)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> {
            documentService.getDocumentById(id);
        });
    }

    @Test
    public void testGetAllVersionsById() {
        // Mock the AccessService to return true for chekRAccess method
        Mockito.when(accessService.chekRAccess(Mockito.anyLong())).thenReturn(true);

        // Mock the DocumentDao to return a list of concrete documents
        List<ConcreteDocumentDto> mockConcreteDocuments = List.of(new ConcreteDocumentDto(), new ConcreteDocumentDto());
        Mockito.when(documentDao.getAllVersions(Mockito.anyLong())).thenReturn(mockConcreteDocuments);

        // Call the getAllVersionsById method with an arbitrary id
        List<ConcreteDocumentDto> actualConcreteDocuments = documentService.getAllVersionsById(1L);

        // Verify that the list of ConcreteDocumentDto returned by the getAllVersionsById method matches the mock list
        assertEquals(mockConcreteDocuments, actualConcreteDocuments);
    }

    @Test
    public void testGetAllVersionsById_withoutAccess() {
        Long id = 1L;

        when(accessService.chekRAccess(id)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> {
            documentService.getAllVersionsById(id);
        });
    }

    @Test
    public void testSaveNewDocumentAccessDenied() {
        // mock data
        DocumentDto documentDto = DocumentDto.builder().build();
        ConcreteDocumentDto concreteDocumentDto = new ConcreteDocumentDto();

        // mock accessService behavior
        when(accessService.chekRWAccess(documentDto.getParentId())).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> documentService.saveNewDocument(documentDto, concreteDocumentDto));
    }

    @Test
    public void saveNewDocument_validAccess_shouldReturnDocumentDto() {
        // Arrange
        DocumentDto documentDto = DocumentDto.builder().build();
        documentDto.setParentId(1L);
        ConcreteDocumentDto concreteDocumentDto = new ConcreteDocumentDto();
        concreteDocumentDto.setParentDocumentId(1L);
        UserDto user = UserDto.builder().id(1L).role("ADMIN").build();
        when(userService.getCurrentUser()).thenReturn(user);
        when(accessService.chekRWAccess(documentDto.getParentId())).thenReturn(true);
        when(documentDao.addNewDocument(documentDto, concreteDocumentDto)).thenReturn(documentDto);

        // Act
        DocumentDto result = documentService.saveNewDocument(documentDto, concreteDocumentDto);

        // Assert
        assertEquals(documentDto, result);
        assertEquals(documentDto.getUserCreatedById(), userService.getCurrentUser().getId());
    }

    @Test
    public void testModifyDocumentWithRWAccess() {
        ConcreteDocumentDto concreteDocumentDto = new ConcreteDocumentDto();
        concreteDocumentDto.setParentDocumentId(123L);
        when(accessService.chekRWAccess(concreteDocumentDto.getParentDocumentId())).thenReturn(true);
        when(userService.getCurrentUser()).thenReturn(UserDto.builder().id(456L).role("USER").build());
        when(documentDao.modifyDocument(concreteDocumentDto)).thenReturn(DocumentDto.builder().build());

        DocumentDto result = documentService.modifyDocument(concreteDocumentDto);

        verify(accessService, times(1)).chekRWAccess(concreteDocumentDto.getParentDocumentId());
        verify(userService, times(1)).getCurrentUser();
        verify(documentDao, times(1)).modifyDocument(concreteDocumentDto);
    }

    @Test
    public void testModifyDocumentWithoutRWAccess() {
        ConcreteDocumentDto concreteDocumentDto = new ConcreteDocumentDto();
        concreteDocumentDto.setParentDocumentId(123L);
        when(accessService.chekRWAccess(concreteDocumentDto.getParentDocumentId())).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> documentService.modifyDocument(concreteDocumentDto));
    }

    @Test
    public void testDeleteDocumentByIdWithRWAccess() {
        Long documentId = 123L;
        when(accessService.chekRWAccess(documentId)).thenReturn(true);

        documentService.deleteDocumentById(documentId);

        verify(accessService, times(1)).chekRWAccess(documentId);
        verify(documentDao, times(1)).deleteDocument(documentId);
    }

    @Test
    public void testDeleteDocumentByIdWithoutRWAccess() {
        Long documentId = 123L;
        when(accessService.chekRWAccess(documentId)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> documentService.deleteDocumentById(documentId));
    }
}