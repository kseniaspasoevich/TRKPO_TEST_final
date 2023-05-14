package documents.service.service;

import documents.dao.UserDao;
import documents.dto.user.UserDto;
import documents.jpa.exceprions.IdNotFoundException;
import documents.jpa.repository.UserRepository;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDao userDaoJpa;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findUserByLogin(s).orElseThrow(IdNotFoundException::new);
    }

    public UserDto addNewUser(UserDto userDto){

        return userDaoJpa.addNewUser(userDto);
    }

    public UserDto getCurrentUser(){
        return userDaoJpa.getCurrentUser();
    }

    public UserDto modifyUser(UserDto userDto) {
        return userDaoJpa.modifyUser(userDto);
    }

    public List<UserDto> getAllUsers() {
        return userDaoJpa.getAllUsers();
    }

    public UserDto getUserById(Long id) {
        return userDaoJpa.getUserById(id);
    }

}
