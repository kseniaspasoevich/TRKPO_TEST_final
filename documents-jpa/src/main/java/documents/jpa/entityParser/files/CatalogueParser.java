package documents.jpa.entityParser.files;

import documents.dto.files.catalogues.CatalogueDto;
import documents.jpa.entity.files.catalogues.Catalogue;
import documents.jpa.entity.user.User;
import documents.jpa.exceprions.IdNotFoundException;
import documents.jpa.repository.CatalogueRepository;
import documents.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Component
public class CatalogueParser {

    @Autowired
    CatalogueRepository catalogueRepository;
    @Autowired
    UserRepository userRepository;

    public CatalogueDto EtoDTO(Catalogue catalogue) {
        Long id = null;
        if(catalogue.getParentCatalogue() != null)
            id = catalogue.getParentCatalogue().getId();

        return CatalogueDto.builder()
                .id(catalogue.getId())
                .createdTime(new Timestamp(catalogue.getCreatedTime().getTime()))
                .userCreatedById(catalogue.getUserCreatedBy() == null ? null : catalogue.getUserCreatedBy().getId())
                .parentId(catalogue.getParentCatalogue() == null ? null : catalogue.getParentCatalogue().getId())
                .name(catalogue.getName()).typeOfFile("CATALOGUE").build();
    }


    public Catalogue DTOtoE(CatalogueDto catalogueDto){

        Catalogue catalogueParent = catalogueDto.getParentId() == null ?
                null :
                catalogueRepository.findById(catalogueDto.getParentId()).orElseThrow(IdNotFoundException::new);

        User user = catalogueDto.getUserCreatedById() == null ?
                null:
                userRepository.findById(catalogueDto.getUserCreatedById()).orElseThrow(IdNotFoundException::new);

        Catalogue catalogue =  new Catalogue();
        catalogue.setId(catalogueDto.getId());
        catalogue.setCreatedTime(new Date());
        catalogue.setName(catalogueDto.getName());
        catalogue.setParentCatalogue(catalogueParent);
        catalogue.setUserCreatedBy(user);
        return catalogue;
    }

    public List<CatalogueDto> fromList(List<Catalogue> list){
        List<CatalogueDto> catalogueDto = new LinkedList<>();
        list.forEach(v ->{
            catalogueDto.add(EtoDTO(v));
        });
        return catalogueDto;
    }
}