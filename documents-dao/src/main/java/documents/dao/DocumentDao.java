package documents.dao;

import documents.dto.files.documents.ConcreteDocumentDto;
import documents.dto.files.documents.DocumentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DocumentDao extends AbstractDao {
    List<DocumentDto> getAllDocuments();

    Page<DocumentDto> getAllDocuments(Pageable paging, String name, String documentType);

    DocumentDto getDocumentById(Long id);

    List<ConcreteDocumentDto> getAllVersions(Long id);

    DocumentDto addNewDocument(DocumentDto documentDto, ConcreteDocumentDto concreteDocumentDto);

    DocumentDto modifyDocument(ConcreteDocumentDto concreteDocumentDto);

    Long deleteDocument(Long id);
}
