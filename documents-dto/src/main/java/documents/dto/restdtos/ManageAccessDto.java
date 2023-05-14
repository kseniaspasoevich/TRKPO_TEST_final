package documents.dto.restdtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class ManageAccessDto {

    @NotNull
    private TypeOfAccess access;
    @NotNull
    private TypeOfModifying modify;
    @NotNull
    private Long fileId;
    @NotNull
    private Long userId;

    public enum TypeOfModifying {
        DECLINE, GRANT;
    }
    public enum TypeOfAccess {
        READ, READWRITE;
    }
}
