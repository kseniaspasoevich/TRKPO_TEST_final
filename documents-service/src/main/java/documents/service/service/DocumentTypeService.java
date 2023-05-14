package documents.service.service;

import documents.dao.DocumentTypeDao;
import documents.dto.files.documents.DocumentTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentTypeService {

    @Autowired
    DocumentTypeDao documentTypeDao;

    public List<DocumentTypeDto> getAllDocumentTypes(){
        return documentTypeDao.getAllDocumentTypes();
    }

    public DocumentTypeDto addDocumentType(DocumentTypeDto documentTypeDto){
        return documentTypeDao.addNewDocumentType(documentTypeDto);
    }

    public DocumentTypeDto getDocumentTypeById(Long id) {
        return documentTypeDao.getDocumentTypeById(id);
    }

    public void deleteById(Long id) {
        documentTypeDao.deleteDocumentType(id);
    }

    public DocumentTypeDto modifyType(DocumentTypeDto documentTypeDto) {
        return documentTypeDao.modifyDocument(documentTypeDto);
    }

}
