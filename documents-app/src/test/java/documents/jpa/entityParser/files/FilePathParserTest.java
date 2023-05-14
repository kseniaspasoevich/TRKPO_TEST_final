package documents.jpa.entityParser.files;

import documents.dto.files.documents.ConcreteDocumentDto;
import documents.dto.files.documents.FilePathDto;
import documents.jpa.entity.files.documents.ConcreteDocument;
import documents.jpa.entity.files.documents.FilePath;
import documents.jpa.entityParser.files.FilePathParser;
import documents.jpa.exceprions.IdNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class FilePathParserTest {

    @Mock
    private FilePath filePath;
    @Mock
    private ConcreteDocument parent;

    @InjectMocks
    private FilePathParser filePathParser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEtoDTO_NullFilePath() {
        // Mock input data
        when(filePath.getPath()).thenReturn(null);
        when(filePath.getName()).thenReturn(null);
        when(filePath.getCreatedTime()).thenReturn(null);
        when(filePath.getId()).thenReturn(null);
        when(filePath.getSize()).thenReturn(null);

        // Test the method
        FilePathDto resultDto = filePathParser.EtoDTO(filePath);

        // Verify the result
        assertEquals(null, resultDto.getPath());
        assertEquals(null, resultDto.getName());
        assertEquals(null, resultDto.getCreatedTime());
        assertEquals(null, resultDto.getId());
        assertEquals(null, resultDto.getSize());
    }


    @Test
    void testFromList_EmptyList() {
        // Mock input data
        List<FilePath> filePaths = new ArrayList<>();

        // Test the method
        List<FilePathDto> resultDtos = filePathParser.fromList(filePaths);

        // Verify the result
        assertEquals(0, resultDtos.size());
    }

    @Test
    void testFromList_MultipleFilePaths() {
        // Mock input data
        FilePath filePath1 = new FilePath();
        filePath1.setPath("/path1");
        filePath1.setName("file1.txt");
        filePath1.setSize(100L);
        filePath1.setParent(parent);
        filePath1.setCreatedTime(new Date());

        FilePath filePath2 = new FilePath();
        filePath2.setPath("/path2");
        filePath2.setName("file2.txt");
        filePath2.setSize(200L);
        filePath2.setParent(parent);
        filePath2.setCreatedTime(new Date());

        List<FilePath> filePaths = Arrays.asList(filePath1, filePath2);

        // Test the method
        List<FilePathDto> resultDtos = filePathParser.fromList(filePaths);

        // Verify the result
        assertEquals(filePaths.size(), resultDtos.size());

        for (int i = 0; i < filePaths.size(); i++) {
            FilePath filePath = filePaths.get(i);
            FilePathDto dto = resultDtos.get(i);

            assertEquals(filePath.getPath(), dto.getPath());
            assertEquals(filePath.getName(), dto.getName());
            assertEquals(filePath.getSize(), dto.getSize());
            assertEquals(filePath.getParent(), parent);
            assertEquals(filePath.getCreatedTime(), dto.getCreatedTime());
        }
    }

}
