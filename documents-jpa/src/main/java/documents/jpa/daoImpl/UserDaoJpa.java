package documents.jpa.daoImpl;

import documents.dao.UserDao;
import documents.dto.user.UserDto;
import documents.jpa.entity.user.User;
import documents.jpa.entity.user.UserRolesEnum;
import documents.jpa.entityParser.user.UserParser;
import documents.jpa.exceprions.ConstraintsException;
import documents.jpa.exceprions.IdNotFoundException;
import documents.jpa.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserDaoJpa implements UserDao {

    @Autowired
    UserParser userParser;
    @Autowired
    UserRepository userRepository;
    @PersistenceContext
    private EntityManager em;

    @Override
    public UserDto getCurrentUser() {
        return userParser.EtoDTO(
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );
    }

    @Override
    @Transactional
    public UserDto addNewUser(UserDto userDto) {
        User user = userParser.DTOtoE(userDto);
        user.setRole(UserRolesEnum.USER); // EVERYONE "USER" by default

        try {
            em.persist(user);
        } catch (Exception e){
            log.error(e.getMessage());
            throw new ConstraintsException();
        }
        return userParser.EtoDTO(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userParser::EtoDTO).collect(Collectors.toList());
    }

    @Override
    public Long deleteUser(Long id) {
        return null;
    }

    @Override
    public UserDto getUserByLogin(String login) {
        return null;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(IdNotFoundException::new);
        return userParser.EtoDTO(user);
    }


    @Override
    @Transactional
    public UserDto modifyUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(IdNotFoundException::new);

        if (userDto.getRole() != null)
            user.setRole(UserRolesEnum.valueOf(userDto.getRole()));
        if (userDto.getLogin() != null)
            user.setLogin(userDto.getLogin());
        if (userDto.getPassword() != null)
            user.setPassword(userDto.getPassword());

        try {
            em.merge(user);
        } catch (Exception e){
            log.error(e.getMessage());
            throw new ConstraintsException();
        }
        return userParser.EtoDTO(user);
    }
}
