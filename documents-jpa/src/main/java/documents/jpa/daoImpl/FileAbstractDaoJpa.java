package documents.jpa.daoImpl;

import documents.dao.FileAbstractDao;
import documents.dto.restdtos.ManageAccessDto;
import documents.dto.user.UserDto;
import documents.jpa.entity.files.FileAbstract;
import documents.jpa.entity.user.User;
import documents.jpa.entityParser.user.UserParser;
import documents.jpa.exceprions.IdNotFoundException;
import documents.jpa.repository.FileAbstractRepository;
import documents.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileAbstractDaoJpa implements FileAbstractDao {

    @Autowired
    FileAbstractRepository fileAbstractRepository;
    @Autowired
    UserRepository userRepository;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    UserParser userParser;

    /**
     * Grant access to current user
     * If current file (or any parent file) creator current user
     * or
     * If current user has permissions to read current file (or any of parent file)
     *   (in other words he exists in any of ReadWritePermission lists)
     *
     */
    @Override
    @Transactional
    public boolean checkRWAccess(Long id) {
        FileAbstract fileAbstract = fileAbstractRepository.findById(id).orElseThrow(IdNotFoundException::new);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        em.merge(fileAbstract);
        em.merge(user);
        while (fileAbstract != null){
            if (user.equals(fileAbstract.getUserCreatedBy()) ||
            fileAbstract.getReadWritePermissionUsers().contains(user))
                return true;
            else
                fileAbstract = fileAbstract.getParentCatalogue();
        }
        return false;
    }

    /**
     * Grant access to current user
     * If current file (or any parent file) creator is current user
     * or
     * If current user has permissions to read current file (or any of parent file)
     * or
     * If current file and avery parent file (up to root) don't have restrictions on read
     *   (in other words ReadPermission list empty for each)
     *   coz by task Read permission for everyone by default
     *
     */
    @Override
    @Transactional
    public boolean checkRAccess(Long id) {
        FileAbstract fileAbstract = fileAbstractRepository.findById(id).orElseThrow(IdNotFoundException::new);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        em.merge(fileAbstract);
        em.merge(user);
        while (fileAbstract != null){
            if (user.equals(fileAbstract.getUserCreatedBy()) ||
                    fileAbstract.getReadPermissionUsers().contains(user))
                return true;
            else if (fileAbstract.getReadPermissionUsers().size() == 0)
                fileAbstract = fileAbstract.getParentCatalogue();
            else
                return false;
        }
        return true;
    }

    @Override
    @Transactional
    public List<UserDto> manageAccess(ManageAccessDto manageAccessDto) {
        FileAbstract fileAbstract = fileAbstractRepository.findById(manageAccessDto.getFileId()).orElseThrow(IdNotFoundException::new);
        User user = userRepository.findById(manageAccessDto.getUserId()).orElseThrow(IdNotFoundException::new);
        em.merge(fileAbstract);
        em.merge(user);

        if (manageAccessDto.getAccess() == ManageAccessDto.TypeOfAccess.READ){
            if (manageAccessDto.getModify() == ManageAccessDto.TypeOfModifying.GRANT){
                fileAbstract.getReadPermissionUsers().add(user);
            } else {
                fileAbstract.getReadPermissionUsers().remove(user);
            }
            return userParser.fromList(new ArrayList<>(fileAbstract.getReadPermissionUsers()));
        } else {
            if (manageAccessDto.getModify() == ManageAccessDto.TypeOfModifying.GRANT){
                fileAbstract.getReadWritePermissionUsers().add(user);
            } else {
                fileAbstract.getReadWritePermissionUsers().remove(user);
            }
            return userParser.fromList(new ArrayList<>(fileAbstract.getReadWritePermissionUsers()));
        }
    }

}
