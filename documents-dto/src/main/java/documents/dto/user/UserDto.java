package documents.dto.user;

import documents.dto.AbstractDto;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDto implements AbstractDto {
    private Long id;
    @NotBlank(message = "wrong login")
    private String login;
    @NotBlank(message = "wrong password")
    @Size(min = 4, message = "Password must contain at least 4 ch.")
    private String password;
    private String role;
}
