package documents.jpa.daoImpl;

import documents.dao.FileAbstractDao;
import documents.dto.restdtos.ManageAccessDto;
import documents.dto.user.UserDto;
import documents.jpa.daoImpl.FileAbstractDaoJpa;
import documents.jpa.entity.files.FileAbstract;
import documents.jpa.entity.user.User;
import documents.jpa.entityParser.user.UserParser;
import documents.jpa.exceprions.IdNotFoundException;
import documents.jpa.repository.FileAbstractRepository;
import documents.jpa.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.persistence.EntityManager;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static java.util.List.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileAbstractDaoJpaTest {

    @Mock
    private FileAbstractRepository fileAbstractRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private UserParser userParser;

    @InjectMocks
    private FileAbstractDaoJpa fileAbstractDaoJpa;
    private User mockUser;
    private FileAbstract mockFileAbstract;
    private Authentication authentication;

    @InjectMocks
    private FileAbstractDaoJpa fileAbstractDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        mockUser = new User();
        mockUser.setId(1L);

        mockFileAbstract = new FileAbstract();
        mockFileAbstract.setId(1L);
        mockFileAbstract.setUserCreatedBy(mockUser);
    }

    @Test
    @DisplayName("Test checkAccess")
    public void testCheckAccess() {
        // Arrange
        FileAbstract fileAbstract = new FileAbstract();
        User user = new User();
        user.setId(1L);
        when(fileAbstractRepository.findById(1L)).thenReturn(Optional.of(fileAbstract));
        when(authentication.getPrincipal()).thenReturn(user);

        // Act and Assert
        boolean hasReadAccess = fileAbstractDaoJpa.checkRAccess(1L);
        boolean hasReadWriteAccess = fileAbstractDaoJpa.checkRWAccess(1L);

        Assertions.assertTrue(hasReadAccess);
        Assertions.assertFalse(hasReadWriteAccess);
    }


    @Test
    void testCheckRWAccessIdNotFound() {
        when(fileAbstractRepository.findById(1L)).thenThrow(new IdNotFoundException());

        assertThrows(IdNotFoundException.class, () -> fileAbstractDaoJpa.checkRWAccess(1L));
    }

    @Test
    void testCheckRWAccess_WithValidId_ShouldReturnTrue() {
        // Arrange
        Long fileId = 1L;
        User user = new User();
        user.setId(1L);

        FileAbstract fileAbstract = new FileAbstract();
        fileAbstract.setId(fileId);
        fileAbstract.setUserCreatedBy(user);

        when(fileAbstractRepository.findById(fileId)).thenReturn(Optional.of(fileAbstract));
        when(entityManager.merge(any())).thenReturn(fileAbstract);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        // Act
        boolean result = fileAbstractDaoJpa.checkRWAccess(fileId);

        // Assert
        assertTrue(result);
        verify(fileAbstractRepository).findById(fileId);
        verify(entityManager, times(2)).merge(any());
        verify(SecurityContextHolder.getContext().getAuthentication(), times(1)).getPrincipal();
    }

    @Test
    void testCheckRWAccess_WithInvalidId_ShouldThrowIdNotFoundException() {
        // Arrange
        Long fileId = 1L;

        when(fileAbstractRepository.findById(fileId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IdNotFoundException.class, () -> fileAbstractDaoJpa.checkRWAccess(fileId));
        verify(fileAbstractRepository).findById(fileId);
        verifyNoMoreInteractions(entityManager, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testCheckRWAccess_WithNoAccess_ShouldReturnFalse() {
        // Arrange
        Long fileId = 1L;
        User user = new User();
        user.setId(1L);

        FileAbstract fileAbstract = new FileAbstract();
        fileAbstract.setId(fileId);

        when(fileAbstractRepository.findById(fileId)).thenReturn(Optional.of(fileAbstract));
        when(entityManager.merge(any())).thenReturn(fileAbstract);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        // Act
        boolean result = fileAbstractDaoJpa.checkRWAccess(fileId);

        // Assert
        assertFalse(result);
        verify(fileAbstractRepository).findById(fileId);
        verify(entityManager, times(2)).merge(any());
        verify(SecurityContextHolder.getContext().getAuthentication(), times(1)).getPrincipal();
    }

    @Test
    void testManageAccess() {
        // Arrange
        ManageAccessDto manageAccessDtoGrant = ManageAccessDto.builder()
                .access(ManageAccessDto.TypeOfAccess.READ)
                .modify(ManageAccessDto.TypeOfModifying.GRANT)
                .fileId(1L)
                .userId(1L)
                .build();

        ManageAccessDto manageAccessDtoRemove = ManageAccessDto.builder()
                .access(ManageAccessDto.TypeOfAccess.READ)
                .modify(ManageAccessDto.TypeOfModifying.DECLINE)
                .fileId(1L)
                .userId(1L)
                .build();

        User user = new User();
        FileAbstract fileAbstract = new FileAbstract();
        Set<User> readPermissionUsers = new HashSet<>();
        readPermissionUsers.add(user);
        fileAbstract.setReadPermissionUsers(readPermissionUsers);

        when(fileAbstractRepository.findById(1L)).thenReturn(Optional.of(fileAbstract));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userParser.fromList(anyList())).thenReturn(new ArrayList<>());

        // Act and Assert
        List<UserDto> result;

        if (manageAccessDtoGrant.getModify() == ManageAccessDto.TypeOfModifying.GRANT) {
            result = fileAbstractDao.manageAccess(manageAccessDtoGrant);
            assertTrue(readPermissionUsers.contains(new User()));
        } else {
            result = fileAbstractDao.manageAccess(manageAccessDtoRemove);
            assertFalse(readPermissionUsers.contains(user));
        }

        verify(fileAbstractRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(userParser, times(1)).fromList(anyList());
        assertNotNull(result);
    }


}
