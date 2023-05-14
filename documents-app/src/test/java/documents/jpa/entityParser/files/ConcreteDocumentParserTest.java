package documents.jpa.entityParser.files;

import documents.dto.files.documents.ConcreteDocumentDto;
import documents.jpa.entity.files.documents.ConcreteDocument;
import documents.jpa.entity.files.documents.Document;
import documents.jpa.entity.user.User;
import documents.jpa.exceprions.IdNotFoundException;
import documents.jpa.repository.DocumentRepository;
import documents.jpa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConcreteDocumentParserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private FilePathParser filePathParser;

    @InjectMocks
    private ConcreteDocumentParser concreteDocumentParser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDTOtoE() {
        // Arrange
        ConcreteDocumentDto dto = ConcreteDocumentDto.builder()
                .id(1L)
                .name("Test Document")
                .description("Test description")
                .version(1L)
                .modifiedTime(new Timestamp(new Date().getTime()))
                .userModifiedBy(1L)
                .parentDocumentId(null)
                .build();

        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        // Act
        ConcreteDocument document = concreteDocumentParser.DTOtoE(dto);

        // Assert
        assertNotNull(document);
        assertEquals(dto.getId(), document.getId());
        assertEquals(dto.getName(), document.getName());
        assertEquals(dto.getDescription(), document.getDescription());
        assertEquals(dto.getVersion(), document.getVersion());
        assertNotNull(document.getModifiedTime());
        assertNotNull(document.getModifiedBy());
    }

    @Test
    void testDTOtoEWithMissingUser() {
        // Arrange
        ConcreteDocumentDto dto = ConcreteDocumentDto.builder()
                .id(1L)
                .name("Test Document")
                .description("Test description")
                .version(1L)
                .modifiedTime(new Timestamp(new Date().getTime()))
                .userModifiedBy(1L)
                .parentDocumentId(null)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IdNotFoundException.class, () -> concreteDocumentParser.DTOtoE(dto));
    }

    @Test
    void testFromListWithEmptyList() {
        // Arrange
        List<ConcreteDocument> documentList = Collections.emptyList();

        // Act
        List<ConcreteDocumentDto> dtoList = concreteDocumentParser.fromList(documentList);

        // Assert
        assertNotNull(dtoList);
        assertTrue(dtoList.isEmpty());
    }
}
