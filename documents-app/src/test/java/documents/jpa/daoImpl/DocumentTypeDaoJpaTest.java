package documents.jpa.daoImpl;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import documents.jpa.daoImpl.DocumentTypeDaoJpa;
import org.junit.jupiter.api.*;
import org.mockito.*;
import documents.dao.*;
import documents.dto.files.documents.*;
import documents.jpa.entity.files.documents.*;
import documents.jpa.exceprions.*;
import documents.jpa.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

public class DocumentTypeDaoJpaTest {

    @Mock
    DocumentTypeRepository documentTypeRepository;

    @Mock
    EntityManager entityManager;

    @InjectMocks
    DocumentTypeDaoJpa documentTypeDaoJpa;
    @Captor
    private ArgumentCaptor<DocumentType> documentTypeCaptor;

//    @InjectMocks
//    private DocumentTypeDaoJpa documentTypeDao;

    @Autowired
    @InjectMocks
    private DocumentTypeDao documentTypeDao = new DocumentTypeDaoJpa();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testGetAllDocumentTypes() {
//        // Arrange
//        List<DocumentType> documentTypes = Arrays.asList(
//                new DocumentType(1L, "type1"),
//                new DocumentType(2L, "type2")
//        );
//        when(documentTypeRepository.findAll()).thenReturn(documentTypes);
//
//        // Act
//        List<DocumentTypeDto> result = documentTypeDaoJpa.getAllDocumentTypes();
//
//        // Assert
//        assertEquals(2, result.size());
//        assertEquals("type1", result.get(0).getName());
//        assertEquals("type2", result.get(1).getName());
//    }

    @Test
    void testDeleteDocumentType() {
        // Arrange
        Long id = 1L;
        doNothing().when(documentTypeRepository).deleteById(id);

        // Act
        Long result = documentTypeDaoJpa.deleteDocumentType(id);

        // Assert
        assertEquals(0L, result);
        verify(documentTypeRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteDocumentTypeWithNullId() {
        // Arrange

        // Act & Assert
        assertThrows(IdNotFoundException.class, () -> documentTypeDaoJpa.deleteDocumentType(null));
        verify(documentTypeRepository, never()).deleteById(anyLong());
    }


    @Test
    public void testGetDocumentTypeById_nonExistingId() {
        // Mocking data
        when(documentTypeRepository.findById(2L)).thenReturn(Optional.empty());

        // Call the method
        DocumentTypeDto result = documentTypeDao.getDocumentTypeById(2L);

        // Verify the result
        assertEquals(null, result.getName());
        assertEquals(null, result.getId());

        // Verify that the repository method was called
        verify(documentTypeRepository, times(1)).findById(2L);
    }

    @Test
    public void testGetDocumentTypeByType_nonExistingType() {
        // Mocking data
        when(documentTypeRepository.findByName("nonexistingtype")).thenReturn(Optional.empty());

        // Call the method
        DocumentTypeDto result = documentTypeDao.getDocumentTypeByType("NonExistingType");

        // Verify the result
        assertEquals(null, result.getName());
        assertEquals(null, result.getId());

        // Verify that the repository method was called
        verify(documentTypeRepository, times(1)).findByName("nonexistingtype");
    }
}
