package documents.jpa.daoImpl;

import documents.dao.UserDao;
import documents.dto.user.UserDto;
import documents.jpa.entity.user.User;
import documents.jpa.entity.user.UserRolesEnum;
import documents.jpa.entityParser.user.UserParser;
import documents.jpa.exceprions.ConstraintsException;
import documents.jpa.exceprions.IdNotFoundException;
import documents.jpa.repository.UserRepository;
import documents.jpa.daoImpl.UserDaoJpa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class UserDaoJpaTest {

    @Mock
    private UserParser userParser;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserDao userDao = new UserDaoJpa();

    private User testUser;
    private UserDto testUserDto;
    @InjectMocks
    UserDaoJpa userDaoJpa;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setLogin("testUser");
        testUser.setPassword("password");
        testUser.setRole(UserRolesEnum.USER);

        testUserDto = UserDto.builder().build();
        testUserDto.setId(1L);
        testUserDto.setLogin("testUser");
        testUserDto.setPassword("password");
        testUserDto.setRole(UserRolesEnum.USER.toString());
    }

    @Test
    void testGetCurrentUser() {
        //given
        Authentication authentication = new UsernamePasswordAuthenticationToken(new User(),
                null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDto userDto = UserDto.builder().build();
        when(userParser.EtoDTO(any())).thenReturn(userDto);

        //when
        UserDto currentUser = userDaoJpa.getCurrentUser();

        //then
        Assertions.assertEquals(userDto, currentUser);
    }

    @Test
    void testAddNewUser() {
        //given
        UserDto userDto = UserDto.builder().build();
        User user = new User();
        when(userParser.DTOtoE(userDto)).thenReturn(user);
        //when(entityManager.persist(any(User.class))).thenReturn(user);
        when(userParser.EtoDTO(any(User.class))).thenReturn(userDto);

        //when
        UserDto newUser = userDaoJpa.addNewUser(userDto);

        //then
        Assertions.assertEquals(userDto, newUser);
    }

    @Test
    void testModifyUser() {
        //given
        UserDto userDto = UserDto.builder().build();
        userDto.setId(1L);
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        when(userParser.EtoDTO(any(User.class))).thenReturn(userDto);

        //when
        UserDto modifiedUser = userDaoJpa.modifyUser(userDto);

        //then
        Assertions.assertEquals(userDto, modifiedUser);
    }

    @Test
    void testModifyUserWithIdNotFoundException() {
        //given
        UserDto userDto = UserDto.builder().build();
        userDto.setId(1L);
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

        //when-then
        Assertions.assertThrows(IdNotFoundException.class, () -> userDaoJpa.modifyUser(userDto));
    }


    @Test
    public void modifyUser_nonExistingUser_throwIdNotFoundException() {
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.empty());

        assertThrows(IdNotFoundException.class, () -> userDao.modifyUser(testUserDto));

        verify(userRepository, times(1)).findById(testUser.getId());
        verify(entityManager, never()).merge(any(User.class));
        verify(userParser, never()).EtoDTO(any(User.class));
        verify(userParser, never()).DTOtoE(any(UserDto.class));
    }

}