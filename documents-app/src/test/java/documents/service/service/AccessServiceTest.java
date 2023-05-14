package documents.service.service;

import static org.junit.jupiter.api.Assertions.*;

import documents.dao.FileAbstractDao;
import documents.dto.restdtos.ManageAccessDto;
import documents.dto.user.UserDto;
import documents.jpa.entity.user.UserRolesEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import static org.mockito.Mockito.*;


import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AccessServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private FileAbstractDao fileAbstractDaoJpa;
    @InjectMocks
    private AccessService accessService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckRWAccess() {
        UserDto user = UserDto.builder().build();
        user.setRole("ADMIN");
        when(userService.getCurrentUser()).thenReturn(user);
        boolean result = accessService.chekRWAccess(1L);
        Assertions.assertTrue(result);

        user.setRole("USER");
        when(fileAbstractDaoJpa.checkRWAccess(1L)).thenReturn(true);
        result = accessService.chekRWAccess(1L);
        Assertions.assertTrue(result);
    }

    @Test
    public void testCheckRAccess() {
        UserDto user = UserDto.builder().build();
        user.setRole("ADMIN");
        when(userService.getCurrentUser()).thenReturn(user);
        boolean result = accessService.chekRAccess(1L);
        Assertions.assertTrue(result);


        user.setRole("USER");
        when(fileAbstractDaoJpa.checkRAccess(1L)).thenReturn(false);
        when(fileAbstractDaoJpa.checkRWAccess(1L)).thenReturn(false);
        result = accessService.chekRAccess(1L);
        Assertions.assertFalse(result);

        user.setRole("USER");
        when(fileAbstractDaoJpa.checkRAccess(1L)).thenReturn(true);
        result = accessService.chekRAccess(1L);
        Assertions.assertTrue(result);

        when(fileAbstractDaoJpa.checkRWAccess(1L)).thenReturn(true);
        result = accessService.chekRAccess(1L);
        Assertions.assertTrue(result);
    }

    @Test
    public void testModifyFileAccess() {
        UserDto user = UserDto.builder().build();
        user.setRole("ADMIN");
        when(userService.getCurrentUser()).thenReturn(user);
        ManageAccessDto manageAccessDto = ManageAccessDto.builder().build();
        manageAccessDto.setFileId(1L);
        List<UserDto> userDtos = Arrays.asList(UserDto.builder().build());
        when(fileAbstractDaoJpa.manageAccess(manageAccessDto)).thenReturn(userDtos);
        List<UserDto> result = accessService.modifyFileAccess(manageAccessDto);
        Assertions.assertEquals(userDtos, result);

        user.setRole("USER");
        when(fileAbstractDaoJpa.checkRWAccess(1L)).thenReturn(true);
        result = accessService.modifyFileAccess(manageAccessDto);
        Assertions.assertEquals(userDtos, result);

        when(fileAbstractDaoJpa.checkRWAccess(1L)).thenReturn(false);
        Assertions.assertThrows(AccessDeniedException.class, () -> {
            accessService.modifyFileAccess(manageAccessDto);
        });
    }

    @Test
    public void testGrantAccessAdminRole() {
        // Arrange
        UserDto currentUser = UserDto.builder().id(1L).role(UserRolesEnum.ADMIN.toString()).build();
        UserDto userToModify = UserDto.builder().id(2L).role(UserRolesEnum.USER.toString()).build();

        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(userService.modifyUser(any(UserDto.class))).thenReturn(userToModify);

        // Act
        UserDto result = accessService.grantAccess(userToModify.getId(), UserRolesEnum.USER);

        // Assert
        assertEquals(userToModify, result);
    }

    @Test
    public void testGrantAccessNonAdminRole() {
        // Arrange
        UserDto currentUser = UserDto.builder().id(1L).role(UserRolesEnum.USER.toString()).build();
        when(userService.getCurrentUser()).thenReturn(currentUser);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> accessService.grantAccess(2L, UserRolesEnum.USER));
    }

    @Test
    public void testGrantAccessAsAdmin() {
        Long id = 123L;
        UserRolesEnum role = UserRolesEnum.USER;
        UserDto currentUser = UserDto.builder().build();
        currentUser.setRole(UserRolesEnum.ADMIN.toString());
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(userService.modifyUser(UserDto.builder().id(id).role(role.toString()).build())).thenReturn(UserDto.builder().build());

        UserDto result = accessService.grantAccess(id, role);

        verify(userService, times(1)).getCurrentUser();
        verify(userService, times(1)).modifyUser(UserDto.builder().id(id).role(role.toString()).build());
    }

    @Test
    public void testGrantAccessAsNonAdmin() {
        Long id = 123L;
        UserRolesEnum role = UserRolesEnum.USER;
        UserDto currentUser = UserDto.builder().build();
        currentUser.setRole(UserRolesEnum.USER.toString());
        when(userService.getCurrentUser()).thenReturn(currentUser);


        assertThrows(AccessDeniedException.class, () -> accessService.grantAccess(id, role));

    }

}