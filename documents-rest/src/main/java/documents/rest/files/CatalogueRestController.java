package documents.rest.files;

import documents.dto.files.FileAbstractDto;
import documents.dto.files.catalogues.CatalogueDto;
import documents.service.service.CatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/catalogue")
public class CatalogueRestController {

    @Autowired
    CatalogueService catalogueService;

    @GetMapping(value = "")
    public List<CatalogueDto> getAllCatalogues() {
        return catalogueService.getAllCatalogues();
    }

    @GetMapping("/{id}")
    public CatalogueDto getCatalogue(@PathVariable Long id){
        return catalogueService.getCatalogueById(id);
    }

    @GetMapping("/root")
    public CatalogueDto getRootCatalogue(){
        return catalogueService.getCatalogueById(null);
    }

    @GetMapping("/test")
    public String getTest(){
        return "Test";
    }

    @GetMapping("/open/{id}")
    public List<FileAbstractDto> openCatalogue(@PathVariable Long id,
                                               @RequestParam(required = false) String type,
                                               @RequestParam(required = false) String name,
                                               @RequestParam(required = false) String documentType){
        return catalogueService.getInnerCataloguesAndDocuments(id, type, name, documentType);
    }

    @PostMapping(value = "",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public CatalogueDto addCatalogue(@RequestBody @Valid CatalogueDto catalogueDto){
        return catalogueService.createCatalogue(catalogueDto);
    }

    @PostMapping(value = "/modify",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public CatalogueDto modifyCatalogue(@RequestBody CatalogueDto catalogueDto){
        return catalogueService.modifyCatalogue(catalogueDto.getId(), catalogueDto.getName());
    }


    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteCatalogue(@PathVariable Long id){
        catalogueService.deleteCatalogueById(id);
        return ResponseEntity.ok("ok");
    }

    @PostMapping(value = "/deleteByNameAndParentId",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteByNameAndParentId(@RequestBody CatalogueDto catalogueDto){
        //catalogueService.deleteCatalogueByNameAndParentId(catalogueDto);
        return ResponseEntity.ok("ok");
    }

}

