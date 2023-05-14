package documents.service.service;

import documents.dao.DocumentDao;
import documents.dao.DocumentTypeDao;
import documents.dto.files.documents.ConcreteDocumentDto;
import documents.dto.files.documents.DocumentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    @Autowired
    UserService userService;
    @Autowired
    AccessService accessService;
    @Autowired
    DocumentDao documentDao;
    @Autowired
    DocumentTypeDao documentTypeDao;

    // Pageable request
    public Page<DocumentDto> getAllDocuments(Integer page, Integer pageSize, String name, String documentType) {
        Pageable paging = PageRequest.of(page, pageSize);
        Page<DocumentDto> pageOfDocuments = documentDao.getAllDocuments(paging, name, documentType);

        return pageOfDocuments;
    }

    public List<DocumentDto> getAllDocuments() {

        return documentDao.getAllDocuments();
    }

    public DocumentDto getDocumentById(Long id) {
        if (!accessService.chekRAccess(id)) {
            throw new AccessDeniedException("Access error");
        }
        return documentDao.getDocumentById(id);
    }

    public List<ConcreteDocumentDto> getAllVersionsById(Long id) {
        if (!accessService.chekRAccess(id)) {
            throw new AccessDeniedException("Access error");
        }
        return documentDao.getAllVersions(id);
    }

    public DocumentDto saveNewDocument(DocumentDto documentDto, ConcreteDocumentDto concreteDocumentDto) {
        if (!accessService.chekRWAccess(documentDto.getParentId())) {
            throw new AccessDeniedException("Access error");
        }
        documentDto.setUserCreatedById(userService.getCurrentUser().getId());
        concreteDocumentDto.setUserModifiedBy(userService.getCurrentUser().getId());
        return documentDao.addNewDocument(documentDto, concreteDocumentDto);
    }

    public DocumentDto modifyDocument(ConcreteDocumentDto concreteDocumentDto) {
        if (!accessService.chekRWAccess(concreteDocumentDto.getParentDocumentId())) {
            throw new AccessDeniedException("Access error");
        }
        concreteDocumentDto.setUserModifiedBy(userService.getCurrentUser().getId());
        return documentDao.modifyDocument(concreteDocumentDto);
    }

    public void deleteDocumentById(Long id) {
        if (!accessService.chekRWAccess(id)) {
            throw new AccessDeniedException("Access error");
        }
        documentDao.deleteDocument(id);
    }
}
