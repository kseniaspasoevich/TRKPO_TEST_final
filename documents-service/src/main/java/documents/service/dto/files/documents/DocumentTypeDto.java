package documents.dto.files.documents;

import documents.dto.AbstractDto;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class DocumentTypeDto implements AbstractDto {
    private Long id;
    @NotBlank
    private String name;
}
