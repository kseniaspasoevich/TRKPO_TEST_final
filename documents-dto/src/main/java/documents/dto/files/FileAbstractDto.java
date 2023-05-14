package documents.dto.files;

import documents.dto.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class FileAbstractDto implements AbstractDto {
    protected Long id;
    @NotNull
    protected Long parentId;
    protected Timestamp createdTime;
    protected Long userCreatedById;
    protected String name;
    protected String typeOfFile;
}
