package documents.jpa.daoImpl;

import documents.dao.DocumentDao;
import documents.dao.FileAbstractDao;
import documents.dto.files.documents.ConcreteDocumentDto;
import documents.dto.files.documents.DocumentDto;
import documents.dto.restdtos.ManageAccessDto;
import documents.jpa.daoImpl.DocumentDaoJpa;
import documents.jpa.daoImpl.FileAbstractDaoJpa;
import documents.jpa.entity.files.FileAbstract;
import documents.jpa.entity.files.documents.ConcreteDocument;
import documents.jpa.entity.files.documents.Document;
import documents.jpa.entity.files.documents.FilePath;
import documents.jpa.entity.user.User;
import documents.jpa.entityParser.files.ConcreteDocumentParser;
import documents.jpa.entityParser.files.DocumentParser;
import documents.jpa.entityParser.files.FilePathParser;
import documents.jpa.exceprions.IdNotFoundException;
import documents.jpa.repository.ConcreteDocumentRepository;
import documents.jpa.repository.DocumentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import documents.jpa.repository.FileAbstractRepository;
import documents.jpa.repository.UserRepository;

import javax.persistence.EntityManager;
import java.util.*;
import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentDaoJpaTest {

    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private ConcreteDocumentRepository concreteDocumentRepository;
    @Mock
    private ConcreteDocumentParser concreteDocumentParser;
    @Mock
    private FilePathParser filePathParser;
    @Mock
    private DocumentParser documentParser;
    @Mock
    private EntityManager em;
    @InjectMocks
    private DocumentDaoJpa documentDaoJpa;

    @InjectMocks
    private FileAbstractDao fileAbstractDao = new FileAbstractDaoJpa();

    private static final Long FILE_ID = 1L;
    private static final Long USER_ID = 2L;
    private DocumentDto documentDto;
    private ConcreteDocumentDto concreteDocumentDto;
    private FileAbstract fileAbstract;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        documentDto = DocumentDto.builder().build();
        documentDto.setId(1L);
        documentDto.setName("testName");

        concreteDocumentDto = new ConcreteDocumentDto();
        concreteDocumentDto.setId(1L);
        concreteDocumentDto.setVersion(1L);
        concreteDocumentDto.setData(new ArrayList<>());

        fileAbstract = new FileAbstract();
        fileAbstract.setId(FILE_ID);

        user = new User();
        user.setId(USER_ID);
    }

    @Test
    void getAllDocumentsTest() {
        // Given
        List<Document> documents = new ArrayList<>();
        documents.add(new Document());
        when(documentRepository.findAll()).thenReturn(documents);
        when(documentParser.fromList(documents)).thenReturn(new ArrayList<>());

        // When
        List<DocumentDto> documentDtos = documentDaoJpa.getAllDocuments();

        // Then
        assertNotNull(documentDtos);
        assertEquals(0, documentDtos.size());
        verify(documentRepository, times(1)).findAll();
        verify(documentParser, times(1)).fromList(documents);
    }

    @Test
    void testGetDocumentById() {
        // Prepare test data
        Long documentId = 1L;
        Document document = new Document();
        document.setId(documentId);
        DocumentDto expectedDto = DocumentDto.builder().build();
        expectedDto.setId(documentId);

        when(documentRepository.findById(anyLong())).thenReturn(Optional.of(document));
        when(documentParser.EtoDTO(any(Document.class))).thenReturn(expectedDto);

        // Perform the test
        DocumentDto result = documentDaoJpa.getDocumentById(documentId);

        // Verify the results
        assertEquals(expectedDto, result);

        // Verify that the mocks were called correctly
        verify(documentRepository).findById(documentId);
        verify(documentParser).EtoDTO(document);
    }

    @Test
    public void testGetDocumentById_ThrowsException() {
        // Arrange
        Long id = 1L;
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IdNotFoundException.class, () -> documentDaoJpa.getDocumentById(id));
        verify(documentRepository, times(1)).findById(id);
    }
    @Test
    void testDeleteDocument() {
        // Prepare test data
        Long documentId = 1L;
        List<ConcreteDocument> concreteDocuments = Arrays.asList(new ConcreteDocument(), new ConcreteDocument());

        when(concreteDocumentRepository.getAllVersions(anyLong())).thenReturn(concreteDocuments);

        // Perform the test
        Long result = documentDaoJpa.deleteDocument(documentId);

        // Verify the results
        assertEquals(0L, result);

        // Verify that the mocks were called correctly
        verify(concreteDocumentRepository).getAllVersions(documentId);
        verify(em, times(concreteDocuments.size())).remove(any(ConcreteDocument.class));
        verify(documentRepository).deleteById(documentId);
    }

    @Test
    void testGetAllVersions() {
        // Prepare test data
        Long documentId = 1L;
        List<ConcreteDocument> concreteDocuments = Arrays.asList(new ConcreteDocument(), new ConcreteDocument());

        when(concreteDocumentRepository.getAllVersions(anyLong())).thenReturn(concreteDocuments);
        when(concreteDocumentParser.fromList(concreteDocuments)).thenReturn(Arrays.asList(new ConcreteDocumentDto(), new ConcreteDocumentDto()));

        // Perform the test
        List<ConcreteDocumentDto> result = documentDaoJpa.getAllVersions(documentId);

        // Verify the results
        assertEquals(2, result.size());

        // Verify that the mocks were called correctly
        verify(concreteDocumentRepository).getAllVersions(documentId);
        verify(concreteDocumentParser).fromList(concreteDocuments);
    }

}
