package documents.service.service;

import documents.dao.FileAbstractDao;
import documents.dto.restdtos.ManageAccessDto;
import documents.dto.user.UserDto;
import documents.jpa.entity.user.UserRolesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessService {

    @Autowired
    UserService userService;
    @Autowired
    FileAbstractDao fileAbstractDaoJpa;

    public boolean chekRWAccess(Long id) {
        if (userService.getCurrentUser().getRole().equals("ADMIN"))
            return true;
        return fileAbstractDaoJpa.checkRWAccess(id);
    }

    public boolean chekRAccess(Long id) {
        if (userService.getCurrentUser().getRole().equals("ADMIN"))
            return true;
        return (fileAbstractDaoJpa.checkRAccess(id) || fileAbstractDaoJpa.checkRWAccess(id));
    }

    public List<UserDto> modifyFileAccess(ManageAccessDto manageAccessDto) {
        if (!chekRWAccess(manageAccessDto.getFileId()))
            throw new AccessDeniedException("You cant modify this file");
        return fileAbstractDaoJpa.manageAccess(manageAccessDto);
    }


    public UserDto grantAccess(Long id, UserRolesEnum role) {
        if (userService.getCurrentUser().getRole().equals("ADMIN"))
            return userService.modifyUser(
                    UserDto.builder().id(id).role(role.toString()).build()
            );
        else
            throw new AccessDeniedException("403 returned");
    }


}
