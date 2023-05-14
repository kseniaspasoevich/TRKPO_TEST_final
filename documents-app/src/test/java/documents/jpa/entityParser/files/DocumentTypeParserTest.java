package documents.jpa.entityParser.files;

import documents.dto.files.documents.ConcreteDocumentDto;
import documents.dto.files.documents.DocumentTypeDto;
import documents.jpa.entity.files.documents.ConcreteDocument;
import documents.jpa.entity.files.documents.DocumentType;
import documents.jpa.entityParser.files.DocumentTypeParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class DocumentTypeParserTest {

    @Mock
    private DocumentType documentTypeMock;

    @InjectMocks
    private ConcreteDocumentParser concreteDocumentParser;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEtoDTO() {
        when(documentTypeMock.getId()).thenReturn(1L);
        when(documentTypeMock.getName()).thenReturn("test");

        DocumentTypeDto expected = DocumentTypeDto.builder()
                .id(1L)
                .name("test")
                .build();

        DocumentTypeDto actual = DocumentTypeParser.EtoDTO(documentTypeMock);

        assertEquals(expected, actual);
    }

    @Test
    void testDTOtoE() {
        DocumentTypeDto documentTypeDto = DocumentTypeDto.builder()
                .name("test")
                .build();

        DocumentType expected = new DocumentType(1L, "test");
        DocumentType actual = DocumentTypeParser.DTOtoE(documentTypeDto);

        assertEquals(expected, actual);
    }

    @Test
    void testFromListWithEmptyList() {
        List<DocumentTypeDto> expected = Collections.emptyList();
        List<DocumentTypeDto> actual = new DocumentTypeParser().fromList(Collections.emptyList());
        assertEquals(expected, actual);
    }

    @Test
    void testEtoDTOWithNullDocument() {
        // Arrange
        ConcreteDocument document = null;

        // Act
        ConcreteDocumentDto dto = concreteDocumentParser.EtoDTO(document);

        // Assert
        assertNull(dto);
    }

}