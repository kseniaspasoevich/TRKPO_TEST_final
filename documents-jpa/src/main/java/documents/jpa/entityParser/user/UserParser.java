package documents.jpa.entityParser.user;

import documents.dto.user.UserDto;
import documents.jpa.entity.user.User;
import documents.jpa.entity.user.UserRolesEnum;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class UserParser {

    public UserDto EtoDTO(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .password(user.getPassword())
                .role(user.getRole().toString())
                .build();
    }

    public User DTOtoE(UserDto userDto){
        return new User(userDto.getId(), userDto.getLogin(), userDto.getPassword(), UserRolesEnum.USER);
    }

    public List<UserDto> fromList(List<User> list) {
        List<UserDto> userDtos = new LinkedList<>();
        list.forEach(v -> {
            userDtos.add(EtoDTO(v));
        });
        return userDtos;
    }

}
