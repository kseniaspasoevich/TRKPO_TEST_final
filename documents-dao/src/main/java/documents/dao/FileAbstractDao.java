package documents.dao;

import documents.dto.restdtos.ManageAccessDto;
import documents.dto.user.UserDto;

import java.util.List;

public interface FileAbstractDao extends AbstractDao{

    boolean checkRWAccess(Long id);
    boolean checkRAccess(Long id);

    List<UserDto> manageAccess(ManageAccessDto manageAccessDto);

}
