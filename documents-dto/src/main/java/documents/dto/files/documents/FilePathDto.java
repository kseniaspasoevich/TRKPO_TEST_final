package documents.dto.files.documents;

import documents.dto.AbstractDto;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Builder
public class FilePathDto implements AbstractDto {
    private Long id;
    private String name;
    private Long size;
    @NotBlank
    private String path;
    private Long parentConcreteDocumentId;
    private Date createdTime;
}
