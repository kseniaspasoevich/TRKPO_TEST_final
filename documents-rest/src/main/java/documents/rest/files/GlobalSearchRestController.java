package documents.rest.files;

import documents.dto.files.catalogues.CatalogueDto;
import documents.dto.files.documents.DocumentDto;
import documents.service.service.CatalogueService;
import documents.service.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/globalsearch")
public class GlobalSearchRestController {

    @Autowired
    DocumentService documentService;
    @Autowired
    CatalogueService catalogueService;

    @GetMapping(value = "/documents")
    public Page<DocumentDto> searchAllDocument(@RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer pageSize,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String documentType) {
        return documentService.getAllDocuments(page, pageSize, name, documentType);
    }

    @GetMapping(value = "/catalogue")
    public Page<CatalogueDto> searchAllCatalogues(@RequestParam(defaultValue = "0") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(required = false) String name) {
        return catalogueService.getAllCatalogues(page, pageSize, name);
    }

}
