package documents.jpa.daoImpl;

import documents.dao.CatalogueDao;
import documents.dto.files.FileAbstractDto;
import documents.dto.files.catalogues.CatalogueDto;
import documents.jpa.daoImpl.CatalogueDaoJpa;
import documents.jpa.entity.files.catalogues.Catalogue;
import documents.jpa.entity.files.documents.Document;
import documents.jpa.entityParser.files.CatalogueParser;
import documents.jpa.entityParser.files.DocumentParser;
import documents.jpa.exceprions.IdNotFoundException;
import documents.jpa.repository.CatalogueRepository;
import documents.jpa.daoImpl.CatalogueDaoJpa;
import documents.jpa.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CatalogueDaoJpaTest {

    @Mock
    private CatalogueRepository catalogueRepository;

    @Mock
    private CatalogueParser catalogueParser;

    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private DocumentParser documentParser;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CatalogueDao catalogueDao = new CatalogueDaoJpa();
    @InjectMocks
    private CatalogueDaoJpa catalogueDaoJpa;

    private Catalogue catalogue;
    private CatalogueDto catalogueDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        catalogue = new Catalogue();
        catalogueDto = CatalogueDto.builder().build();
        catalogueDto.setId(1L);
        catalogueDto.setName("test catalogue");
        catalogue.setName("test catalogue");
        catalogue.setId(1L);
    }

    @Test
    void testGetRootCatalogue() {
        when(catalogueRepository.getRoot()).thenReturn(Optional.of(catalogue));
        when(catalogueParser.EtoDTO(catalogue)).thenReturn(catalogueDto);

        CatalogueDto result = catalogueDao.getRootCatalogue();

        assertEquals(catalogueDto.getId(), result.getId());
        assertEquals(catalogueDto.getName(), result.getName());
    }

    @Test
    void testGetCatalogueById() {
        when(catalogueRepository.findById(1L)).thenReturn(Optional.of(catalogue));
        when(catalogueParser.EtoDTO(catalogue)).thenReturn(catalogueDto);

        CatalogueDto result = catalogueDao.getCatalogueById(1L);

        assertEquals(catalogueDto.getId(), result.getId());
        assertEquals(catalogueDto.getName(), result.getName());
    }

    @Test
    void testGetAllCatalogues() {
        Pageable pageable = Pageable.unpaged();
        Page<Catalogue> cataloguePage = new PageImpl<>(Collections.singletonList(catalogue), pageable, 1L);

        when(catalogueRepository.findAllCataloguesByName("%Test%", pageable)).thenReturn(cataloguePage);
        when(catalogueParser.EtoDTO(catalogue)).thenReturn(catalogueDto);

        Page<CatalogueDto> result = catalogueDao.getAllCatalogues(pageable, "Test");

        assertEquals(1L, result.getTotalElements());
        assertEquals(catalogueDto.getId(), result.getContent().get(0).getId());
        assertEquals(catalogueDto.getName(), result.getContent().get(0).getName());
    }
    @Test
    void testDeleteCatalogue() {
        Long id = 1L;
        when(catalogueRepository.existsById(id)).thenReturn(true);

        Long result = catalogueDao.deleteCatalogue(id);

        verify(catalogueRepository, times(1)).deleteById(id);
        assertEquals(0L, result);
    }

    @Test
    void testAddCatalogue() {
        when(catalogueParser.DTOtoE(catalogueDto)).thenReturn(catalogue);
        when(catalogueParser.EtoDTO(catalogue)).thenReturn(catalogueDto);

        CatalogueDto actualDto = catalogueDaoJpa.addCatalogue(catalogueDto);

        assertEquals(catalogueDto, actualDto);
        verify(entityManager).persist(catalogue);
        verify(catalogueParser).DTOtoE(catalogueDto);
        verify(catalogueParser).EtoDTO(catalogue);
    }

    @Test
    void testModifyCatalogue() {
        Long id = 1L;
        String updatedName = "Updated Catalogue Name";

        CatalogueDto catalogueDto = CatalogueDto.builder().build();
        catalogueDto.setId(id);
        catalogueDto.setName(updatedName);

        Catalogue existingCatalogue = new Catalogue();
        existingCatalogue.setId(id);
        existingCatalogue.setName("Old Catalogue Name");

        when(catalogueRepository.findById(id)).thenReturn(Optional.of(existingCatalogue));
        when(catalogueParser.EtoDTO(existingCatalogue)).thenReturn(catalogueDto);

        CatalogueDto modifiedDto = catalogueDaoJpa.modifyCatalogue(catalogueDto);

        assertEquals(updatedName, modifiedDto.getName());
        verify(catalogueRepository).findById(id);
        verify(catalogueParser).EtoDTO(existingCatalogue);
    }
    @Test
    void testGetAllChildren() {
        // Arrange
        Long catalogueId = 1L;
        List<FileAbstractDto> expectedChildren = new ArrayList<>();
        // Add your expected children to the expectedChildren list

        when(catalogueRepository.getChildrens(anyLong())).thenReturn(new ArrayList<>());
        when(documentRepository.getChildrens(anyLong())).thenReturn(new ArrayList<>());

        // Act
        List<FileAbstractDto> actualChildren = catalogueDao.getAllChildren(catalogueId);

        // Assert
        assertEquals(expectedChildren.size(), actualChildren.size());
        // Add additional assertions for the content of the actualChildren list
    }

}
