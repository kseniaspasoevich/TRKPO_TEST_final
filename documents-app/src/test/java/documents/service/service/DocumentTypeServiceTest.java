package documents.service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import documents.dao.DocumentTypeDao;
import documents.dto.files.documents.DocumentTypeDto;

public class DocumentTypeServiceTest {


    @Mock
    private DocumentTypeDao documentTypeDao;

    @InjectMocks
    private DocumentTypeService documentTypeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    void testGetAllDocumentTypes() {
        // Arrange
        List<DocumentTypeDto> expected = new ArrayList<>();
        expected.add(DocumentTypeDto.builder().id(1L).name("Type1").build());
        expected.add(DocumentTypeDto.builder().id(2L).name("Type2").build());

        when(documentTypeDao.getAllDocumentTypes()).thenReturn(expected);
        List<DocumentTypeDto> actual = documentTypeService.getAllDocumentTypes();
        assertEquals(expected, actual);
    }

    @Test
    void testAddDocumentType() {
        // Arrange
        DocumentTypeDto documentTypeDto = DocumentTypeDto.builder().id(null).name("Type1").build();
        DocumentTypeDto expected = DocumentTypeDto.builder().id(1L).name("Type1").build();
        when(documentTypeDao.addNewDocumentType(documentTypeDto)).thenReturn(expected);
        DocumentTypeDto actual = documentTypeService.addDocumentType(documentTypeDto);
        assertEquals(expected, actual);
    }

}