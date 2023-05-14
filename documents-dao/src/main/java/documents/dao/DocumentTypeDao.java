package documents.dao;

import documents.dto.files.documents.DocumentTypeDto;

import java.util.List;

public interface DocumentTypeDao extends AbstractDao{
    List<DocumentTypeDto> getAllDocumentTypes();
    DocumentTypeDto addNewDocumentType(DocumentTypeDto documentTypeDto);
    DocumentTypeDto getDocumentTypeByType(String name);
    DocumentTypeDto getDocumentTypeById(Long id);
    Long deleteDocumentType(Long id);
    DocumentTypeDto modifyDocument(DocumentTypeDto documentTypeDto);
}
