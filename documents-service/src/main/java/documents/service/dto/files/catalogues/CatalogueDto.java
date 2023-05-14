package documents.dto.files.catalogues;

import documents.dto.files.FileAbstractDto;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CatalogueDto extends FileAbstractDto {

    @Builder
    public CatalogueDto(Long id,
                        Long parentId,
                        Timestamp createdTime,
                        Long userCreatedById,
                        String name,
                        String typeOfFile) {
        super(id, parentId, createdTime, userCreatedById, name, "CATALOGUE");
    }
}
