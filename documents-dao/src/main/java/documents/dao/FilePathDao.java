package documents.dao;

import documents.dto.files.documents.ConcreteDocumentDto;
import documents.dto.files.documents.FilePathDto;

import java.util.List;

public interface FilePathDao extends AbstractDao{
    List<FilePathDto> getAllFilePathOfConcreteDocument(Long id);
    FilePathDto addNewFilePathOfConcreteDocument(FilePathDto filePathDto, ConcreteDocumentDto concreteDocumentDto);
    Long deleteFilePath (Long id);
    FilePathDto getById(Long id);
}
