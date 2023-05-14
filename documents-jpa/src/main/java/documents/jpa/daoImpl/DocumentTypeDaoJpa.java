package documents.jpa.daoImpl;

import documents.dao.DocumentTypeDao;
import documents.dto.files.documents.DocumentTypeDto;
import documents.jpa.entity.files.catalogues.Catalogue;
import documents.jpa.entity.files.documents.DocumentType;
import documents.jpa.entityParser.files.DocumentTypeParser;
import documents.jpa.exceprions.IdNotFoundException;
import documents.jpa.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DocumentTypeDaoJpa implements DocumentTypeDao {

    @Autowired
    DocumentTypeRepository documentTypeRepository;

    @Autowired
    DocumentTypeParser documentTypeParser;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<DocumentTypeDto> getAllDocumentTypes() {
        return documentTypeRepository.findAll().stream().map(v ->
                DocumentTypeDto.builder().name(v.getName()).id(v.getId()).build()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DocumentTypeDto addNewDocumentType(DocumentTypeDto documentTypeDto) {
        DocumentType documentType = documentTypeRepository.findByName(documentTypeDto.getName().toLowerCase()).orElse(null);
        if (documentType == null) {
            documentType = new DocumentType(documentTypeDto.getName().toLowerCase());
            em.persist(documentType);
        }
        return DocumentTypeDto.builder().id(documentType.getId()).name(documentType.getName()).build();
    }

    @Override
    public DocumentTypeDto getDocumentTypeByType(String name) {
        DocumentType documentType = documentTypeRepository.findByName(name.toLowerCase()).orElse(null);
        if (documentType == null) {
            return DocumentTypeDto.builder().build();
        }
        return DocumentTypeDto.builder().id(documentType.getId()).name(documentType.getName()).build();
    }

    @Override
    public DocumentTypeDto getDocumentTypeById(Long id) {
        DocumentType documentType = documentTypeRepository.findById(id).orElse(null);
        if (documentType == null) {
            return DocumentTypeDto.builder().build();
        }
        return DocumentTypeDto.builder().id(documentType.getId()).name(documentType.getName()).build();
    }

    @Override
    public Long deleteDocumentType(Long id) {
        if (id != null)
            documentTypeRepository.deleteById(id);
        else
            throw new IdNotFoundException();
        return 0L;
    }

    @Override
    public DocumentTypeDto modifyDocument(DocumentTypeDto documentTypeDto) {
        DocumentType documentType = documentTypeRepository.findById(documentTypeDto.getId()).orElseThrow(IdNotFoundException::new);
        documentType.setName(documentTypeDto.getName());
        return documentTypeParser.EtoDTO(documentType);
    }
}
