package documents.rest.user;

import documents.dto.files.documents.DocumentTypeDto;
import documents.dto.user.UserDto;
import documents.jpa.entity.user.UserRolesEnum;
import documents.service.service.AccessService;
import documents.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    UserService userService;
    @Autowired
    AccessService accessService;

    @GetMapping(value = "")
    public List<UserDto> getAllUser() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping(value = "")
    public UserDto addNewUser(@RequestBody @Valid UserDto userDto){
        return userService.addNewUser(userDto);
    }

    @PostMapping(value = "/register",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDto register(@RequestBody @Valid UserDto userDto) {
        return userService.addNewUser(userDto);
    }

    @GetMapping(value = "/current",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto getCurrentUser() {
        return userService.getCurrentUser();
    }

    @PostMapping(value = "/grantaccess",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDto grantAccess(@RequestBody UserDto userDto) {
        return accessService.grantAccess(userDto.getId(), UserRolesEnum.valueOf(userDto.getRole()));
    }

    @PostMapping(value = "/login")
    public UserDetails login(@RequestBody String login) {
        return userService.loadUserByUsername(login);
    }

    @PutMapping(value = "/{id}")
    public UserDto modifyUser(@RequestBody @Valid UserDto userDto){
        return userService.modifyUser(userDto);
    }
}
