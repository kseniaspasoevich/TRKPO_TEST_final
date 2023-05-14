package documents.service.service;

import documents.dao.CatalogueDao;
import documents.dto.files.FileAbstractDto;
import documents.dto.files.catalogues.CatalogueDto;
import documents.dto.files.documents.DocumentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class CatalogueService {

    @Autowired
    UserService userService;
    @Autowired
    AccessService accessService;

    @Autowired
    private CatalogueDao catalogueDao;

    public Page<CatalogueDto> getAllCatalogues(Integer page, Integer pageSize, String name) {
        Pageable paging = PageRequest.of(page, pageSize);
        Page<CatalogueDto> pageOfCatalogues = catalogueDao.getAllCatalogues(paging, name);
        return pageOfCatalogues;
    }

    public List<CatalogueDto> getAllCatalogues() {
        return catalogueDao.getAllCatalogues();
    }

    public CatalogueDto getCatalogueById(Long id) {
        CatalogueDto catalogueDto;
        if (id == null) {
            return catalogueDao.getRootCatalogue();
        } else {
            if (!accessService.chekRAccess(id))
                throw new AccessDeniedException("Access error");
            return catalogueDao.getCatalogueById(id);
        }
    }

    public List<FileAbstractDto> getInnerCataloguesAndDocuments(Long id, String type, String name, String documentType) {
        if (!accessService.chekRAccess(id))
            throw new AccessDeniedException("Access error");

        List<FileAbstractDto> list = catalogueDao.getAllChildren(id);
        if (type != null)
            list = list.stream()
                    .filter(v -> v.getTypeOfFile()
                    .equals(type.toUpperCase()))
                    .collect(Collectors.toList());
        if (name != null) {
            Pattern pattern = Pattern.compile(".*" + name + ".*", Pattern.CASE_INSENSITIVE);
            list = list.stream()
                    .filter(v -> pattern.matcher(v.getName()).matches())
                    .collect(Collectors.toList());
        }
        if (documentType != null) {
            list = list.stream()
                    .filter(v -> v.getTypeOfFile().equals("DOCUMENT"))
                    .map(v -> (DocumentDto) v)
                    .filter(v -> v.getDocumentType().equals(documentType.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return list;
    }

    public void deleteCatalogueById(Long id) {
        if (!accessService.chekRWAccess(id))
            throw new AccessDeniedException("You cant delete this file");

        if (id != catalogueDao.getRootCatalogue().getId())
            catalogueDao.deleteCatalogue(id);
    }

    public CatalogueDto createCatalogue(CatalogueDto children) {
        if (!accessService.chekRWAccess(children.getParentId()))
            throw new AccessDeniedException("Access error");

        children.setUserCreatedById(userService.getCurrentUser().getId());
        return catalogueDao.addCatalogue(children);
    }

    public CatalogueDto modifyCatalogue(Long id, String name) {
        if (!accessService.chekRWAccess(id) || id.equals(catalogueDao.getRootCatalogue().getId()))
            throw new AccessDeniedException("You cant modify this file");

        catalogueDao.modifyCatalogue(CatalogueDto.builder().name(name).id(id).build());

        return catalogueDao.getCatalogueById(id);
    }

}
