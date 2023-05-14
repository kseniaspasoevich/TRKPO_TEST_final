package documents.jpa.entityParser.user;

import documents.dto.user.UserDto;
import documents.jpa.entity.user.User;
import documents.jpa.entity.user.UserRolesEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserParserTest {

    @Mock
    private User user;

    @Mock
    private User userMock;


    @InjectMocks
    private UserParser userParser;

    @Test
    public void EtoDTOTest() {
        when(user.getId()).thenReturn(1L);
        when(user.getLogin()).thenReturn("testLogin");
        when(user.getPassword()).thenReturn("testPassword");
        when(user.getRole()).thenReturn(UserRolesEnum.USER);

        UserDto userDto = userParser.EtoDTO(user);

        assertEquals(1L, userDto.getId());
        assertEquals("testLogin", userDto.getLogin());
        assertEquals("testPassword", userDto.getPassword());
        assertEquals("USER", userDto.getRole());
    }

    @Test
    public void fromListTest() {
        User user1 = new User(1L, "testLogin1", "testPassword1", UserRolesEnum.USER);
        User user2 = new User(2L, "testLogin2", "testPassword2", UserRolesEnum.ADMIN);
        List<User> userList = Arrays.asList(user1, user2);

        List<UserDto> userDtos = userParser.fromList(userList);

        assertEquals(2, userDtos.size());
        assertEquals(user1.getId(), userDtos.get(0).getId());
        assertEquals(user1.getLogin(), userDtos.get(0).getLogin());
        assertEquals(user1.getPassword(), userDtos.get(0).getPassword());
        assertEquals(user1.getRole().toString(), userDtos.get(0).getRole());
        assertEquals(user2.getId(), userDtos.get(1).getId());
        assertEquals(user2.getLogin(), userDtos.get(1).getLogin());
        assertEquals(user2.getPassword(), userDtos.get(1).getPassword());
        assertEquals(user2.getRole().toString(), userDtos.get(1).getRole());
    }

    @Test
    public void testFromListEmpty() {
        List<User> userList = Collections.emptyList();
        List<UserDto> userDtos = userParser.fromList(userList);
        Assertions.assertTrue(userDtos.isEmpty());
    }

    @Test
    public void testDTOtoEWithNullValues() {
        UserDto userDto = UserDto.builder()
                .id(null)
                .login(null)
                .password(null)
                .role(null)
                .build();

        User user = userParser.DTOtoE(userDto);

        Assertions.assertNull(user.getId());
        Assertions.assertNull(user.getLogin());
        Assertions.assertNull(user.getPassword());
        Assertions.assertEquals(UserRolesEnum.USER, user.getRole());
    }
}
