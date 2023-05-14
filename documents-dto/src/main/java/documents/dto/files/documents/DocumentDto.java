package documents.dto.files.documents;

import documents.dto.files.FileAbstractDto;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


@Data
public class DocumentDto extends FileAbstractDto {
    @NotBlank
    private String documentType;
    private String priority;
    @NotNull
    private ConcreteDocumentDto concreteDocument;

    @Builder
    public DocumentDto(Long id, Long parentId, Timestamp createdTime, Long userCreatedById, String name, String typeOfFile, String documentType, String priority, ConcreteDocumentDto concreteDocument) {
        super(id, parentId, createdTime, userCreatedById, name, "DOCUMENT");
        this.documentType = documentType;
        this.priority = priority;
        this.concreteDocument = concreteDocument;
    }
}
