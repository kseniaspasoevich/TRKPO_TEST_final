package documents.service.service;

import documents.dao.CatalogueDao;
import documents.dto.files.FileAbstractDto;
import documents.dto.files.catalogues.CatalogueDto;
import documents.dto.files.documents.DocumentDto;
import documents.dto.user.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CatalogueServiceTest {


    @Mock
    private UserService userService;

    @Mock
    private AccessService accessService;

    @Mock
    private CatalogueDao catalogueDao;

    @InjectMocks
    private CatalogueService catalogueService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllCatalogues() {
        List<CatalogueDto> catalogueList = new ArrayList<>();
        catalogueList.add(CatalogueDto.builder().build());
        catalogueList.add(CatalogueDto.builder().build());
        Page<CatalogueDto> expectedPage = new PageImpl<>(catalogueList);

        when(catalogueDao.getAllCatalogues(any(), anyString())).thenReturn(expectedPage);

        Page<CatalogueDto> actualPage = catalogueService.getAllCatalogues(1, 10, "test");

        assertEquals(expectedPage, actualPage);
        verify(catalogueDao, times(1)).getAllCatalogues(any(), anyString());
    }

    @Test
    void testGetCatalogueById_withNullId() {
        CatalogueDto expectedCatalogue = CatalogueDto.builder().build();
        when(catalogueDao.getRootCatalogue()).thenReturn(expectedCatalogue);

        CatalogueDto actualCatalogue = catalogueService.getCatalogueById(null);

        assertEquals(expectedCatalogue, actualCatalogue);
        verify(catalogueDao, times(1)).getRootCatalogue();
        verify(accessService, never()).chekRAccess(anyLong());
        verify(catalogueDao, never()).getCatalogueById(anyLong());
    }

//    @Test
//    void deleteCatalogueByNameAndParentId() {
//        catalogueService.deleteCatalogueByNameAndParentId(null);
//    }

    @Test
    void testGetCatalogueById_withNonNullIdAndAccessAllowed() {
        Long id = 1L;
        CatalogueDto expectedCatalogue = CatalogueDto.builder().build();
        when(accessService.chekRAccess(id)).thenReturn(true);
        when(catalogueDao.getCatalogueById(id)).thenReturn(expectedCatalogue);

        CatalogueDto actualCatalogue = catalogueService.getCatalogueById(id);

        assertEquals(expectedCatalogue, actualCatalogue);
        verify(catalogueDao, times(1)).getCatalogueById(id);
        verify(accessService, times(1)).chekRAccess(id);
    }

    @Test
    void testGetCatalogueById_withNonNullIdAndAccessDenied() {
        Long id = 1L;
        when(accessService.chekRAccess(id)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> catalogueService.getCatalogueById(id));
        verify(catalogueDao, never()).getCatalogueById(anyLong());
        verify(accessService, times(1)).chekRAccess(id);
    }

    @Test
    void testGetInnerCataloguesAndDocuments() {
        Long id = 1L;
        String type = "CATALOGUE";
        String name = "document1";
        String documentType = "PDF";

        // Mock data
        CatalogueDto catalogue = CatalogueDto.builder().id(id).name("catalogue1").build();
        DocumentDto document1 = DocumentDto.builder().id(2L).name("document1").documentType("PDF").build();
        DocumentDto document2 = DocumentDto.builder().id(3L).name("document2").documentType("PNG").build();
        CatalogueDto innerCatalogue = CatalogueDto.builder().id(4L).name("innerCatalogue").build();
        List<FileAbstractDto> mockList = List.of(catalogue, document1, document2, innerCatalogue);

        // Mock behaviour
        when(accessService.chekRAccess(id)).thenReturn(true);
        when(catalogueDao.getAllChildren(id)).thenReturn(mockList);

        // Test with all parameters null
        List<FileAbstractDto> result = catalogueService.getInnerCataloguesAndDocuments(id, null, null, null);
        assertEquals(4, result.size());
        assertTrue(result.containsAll(mockList));

        // Test with type specified
        result = catalogueService.getInnerCataloguesAndDocuments(id, type, null, null);
        assertEquals(2, result.size());
        assertTrue(result.contains(catalogue));

        // Test with name specified
        result = catalogueService.getInnerCataloguesAndDocuments(id, null, name, null);
        assertEquals(1, result.size());
        assertTrue(result.contains(document1));


        // Test with document type specified
        result = catalogueService.getInnerCataloguesAndDocuments(id, null, null, documentType);
        //assertEquals(1, result.size());
        //assertTrue(result.contains(document1));
    }

    @Test
    void testGetInnerCataloguesAndDocuments_withAccessDenied() {
        Long id = 1L;
        when(accessService.chekRAccess(id)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> catalogueService.getInnerCataloguesAndDocuments(id, null, null, null));

    }

    @Test
    void deleteCatalogueById_rwAccess() {
        Long id = 1L;
        when(accessService.chekRWAccess(id)).thenReturn(true);
        when(catalogueDao.getRootCatalogue()).thenReturn(CatalogueDto.builder().id(0L).build());

        assertDoesNotThrow(() -> catalogueService.deleteCatalogueById(id));

        verify(accessService).chekRWAccess(id);
        verify(catalogueDao).deleteCatalogue(id);
    }

    @Test
    void deleteCatalogueById_noRWAccess() {
        Long id = 1L;
        when(accessService.chekRWAccess(id)).thenReturn(false);

        assertThrows(AccessDeniedException.class, () -> catalogueService.deleteCatalogueById(id));

        verify(accessService).chekRWAccess(id);
        verify(catalogueDao, never()).deleteCatalogue(id);
    }

    @Test
    void deleteCatalogueById_isRootCatalogue() {
        Long id = 0L;
        when(accessService.chekRWAccess(id)).thenReturn(true);
        when(catalogueDao.getRootCatalogue()).thenReturn(CatalogueDto.builder().id(0L).build());

        assertDoesNotThrow(() -> catalogueService.deleteCatalogueById(id));

        verify(accessService).chekRWAccess(id);
        verify(catalogueDao, never()).deleteCatalogue(id);
    }


    @Test
    void testCreateCatalogue_withRWAccess() {
        CatalogueDto parent = CatalogueDto.builder().id(1L).userCreatedById(1L).build();
        CatalogueDto children = CatalogueDto.builder().name("test catalogue").parentId(parent.getId()).build();
        UserDto user = UserDto.builder().id(1L).role("ADMIN").build();
        when(userService.getCurrentUser()).thenReturn(user);
        when(accessService.chekRWAccess(parent.getId())).thenReturn(true);
        when(catalogueDao.addCatalogue(children)).thenReturn(children);
        CatalogueDto result = catalogueService.createCatalogue(children);
        assertNotNull(result);
        assertEquals("test catalogue", result.getName());
        assertEquals(parent.getId(), result.getParentId());
        assertEquals(user.getId(), result.getUserCreatedById());
        verify(accessService).chekRWAccess(parent.getId());
        verify(catalogueDao).addCatalogue(children);
    }

    @Test
    void testCreateCatalogue_withoutRWAccess() {
        CatalogueDto parent = CatalogueDto.builder().id(1L).build();
        CatalogueDto children = CatalogueDto.builder().name("test catalogue").parentId(parent.getId()).build();
        when(accessService.chekRWAccess(parent.getId())).thenReturn(false);
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> catalogueService.createCatalogue(children));
        assertEquals("Access error", exception.getMessage());
        verify(accessService).chekRWAccess(parent.getId());
        verifyNoInteractions(userService, catalogueDao);
    }

    @Test
    void testModifyCatalogue_withRWAccess() {
        CatalogueDto catalogue = CatalogueDto.builder().id(1L).name("new name").parentId(2L).build();
        when(accessService.chekRWAccess(catalogue.getId())).thenReturn(true);
        when(catalogueDao.getRootCatalogue()).thenReturn(CatalogueDto.builder().id(2L).build());
        when(catalogueDao.modifyCatalogue(any(CatalogueDto.class))).thenReturn(CatalogueDto.builder().id(1L).name("new name").parentId(2L).build());
        when(catalogueDao.getCatalogueById(catalogue.getId())).thenReturn(catalogue);
        CatalogueDto result = catalogueService.modifyCatalogue(catalogue.getId(), "new name");
        assertNotNull(result);
        assertEquals(catalogue.getId(), result.getId());
        assertEquals("new name", result.getName());
        assertEquals(catalogue.getParentId(), result.getParentId());
        verify(accessService).chekRWAccess(catalogue.getId());
        verify(catalogueDao).modifyCatalogue(any(CatalogueDto.class));
        verify(catalogueDao).getCatalogueById(catalogue.getId());
    }


    @Test
    void testModifyCatalogueAccessDenied() {
        // Arrange
        Long id = 1L;
        String name = "new name";
        when(accessService.chekRWAccess(id)).thenReturn(false);
        when(catalogueDao.getRootCatalogue()).thenReturn(CatalogueDto.builder().id(1L).build());

        assertThrows(AccessDeniedException.class, () -> catalogueService.modifyCatalogue(id, name));
    }

    @Test
    void testModifyCatalogueAccessDeniedRootCatalogue() {
        // Arrange
        Long id = 1L;
        String name = "new name";
        when(accessService.chekRWAccess(id)).thenReturn(true);
        when(catalogueDao.getRootCatalogue()).thenReturn(CatalogueDto.builder().id(1L).build());

        assertThrows(AccessDeniedException.class, () -> catalogueService.modifyCatalogue(id, name));
    }

}