package documents.jpa.entityParser.files;

import documents.dto.files.documents.ConcreteDocumentDto;
import documents.jpa.entity.files.documents.ConcreteDocument;
import documents.jpa.entity.files.documents.Document;
import documents.jpa.entity.user.User;
import documents.jpa.exceprions.IdNotFoundException;
import documents.jpa.repository.DocumentRepository;
import documents.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Component
public class ConcreteDocumentParser {

    @Autowired
    UserRepository userRepository;
    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    FilePathParser filePathParser;

    public ConcreteDocumentDto EtoDTO(ConcreteDocument document) {
        if (document == null)
            return null;

        return ConcreteDocumentDto.builder()
                .id(document.getId())
                .parentDocumentId(document.getParent() == null ? null : document.getParent().getId())
                .version(document.getVersion())
                .modifiedTime(new Timestamp(document.getModifiedTime().getTime()))
                .description(document.getDescription())
                .userModifiedBy(document.getModifiedBy() == null ? null : document.getModifiedBy().getId())
                .name(document.getName())
                .data(document.getFilePathList() == null ? null : filePathParser.fromList(document.getFilePathList()))
                .build();
    }

    public ConcreteDocument DTOtoE(ConcreteDocumentDto concreteDocumentDto) {

        User user = concreteDocumentDto.getUserModifiedBy() == null ?
                null :
                userRepository.findById(concreteDocumentDto.getUserModifiedBy()).orElseThrow(IdNotFoundException::new);

        Document document = concreteDocumentDto.getParentDocumentId() == null ?
                null :
                documentRepository.findById(concreteDocumentDto.getParentDocumentId())
                .orElseThrow(IdNotFoundException::new);

        ConcreteDocument concreteDocument = new ConcreteDocument();
        concreteDocument.setId(concreteDocumentDto.getId());
        concreteDocument.setName(concreteDocumentDto.getName());
        concreteDocument.setDescription(concreteDocumentDto.getDescription());
        concreteDocument.setVersion(concreteDocumentDto.getVersion());
        concreteDocument.setModifiedTime(new Date());
        concreteDocument.setModifiedBy(user);

        return concreteDocument;
    }

        public List<ConcreteDocumentDto> fromList(List<ConcreteDocument> list) {
        List<ConcreteDocumentDto> concreteDocumentDto = new LinkedList<>();
        list.forEach(v -> {
            concreteDocumentDto.add(EtoDTO(v));
        });
        return concreteDocumentDto;
    }
}
