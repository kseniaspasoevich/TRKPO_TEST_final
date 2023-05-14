package documents.dao;

import documents.dto.user.UserDto;

import java.util.List;

public interface UserDao extends AbstractDao{
    UserDto getCurrentUser();
    UserDto addNewUser(UserDto userDto);
    List<UserDto> getAllUsers();
    Long deleteUser(Long id);
    UserDto getUserByLogin(String login);
    UserDto getUserById(Long id);
    UserDto modifyUser(UserDto userDto);
}
