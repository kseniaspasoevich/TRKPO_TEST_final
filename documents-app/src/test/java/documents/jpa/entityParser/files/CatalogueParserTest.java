package documents.jpa.entityParser.files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.util.*;

import documents.jpa.entityParser.files.CatalogueParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import documents.dto.files.catalogues.CatalogueDto;
import documents.jpa.entity.files.catalogues.Catalogue;
import documents.jpa.entity.user.User;
import documents.jpa.exceprions.IdNotFoundException;
import documents.jpa.repository.CatalogueRepository;
import documents.jpa.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CatalogueParserTest {

    @Mock
    private CatalogueRepository catalogueRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CatalogueParser catalogueParser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testDTOtoE() {
        // Given
        CatalogueDto catalogueDto = CatalogueDto.builder()
                .id(1L)
                .name("Catalogue 1")
                .parentId(2L)
                .userCreatedById(3L)
                .build();

        Catalogue parentCatalogue = new Catalogue();
        parentCatalogue.setId(2L);

        User user = new User();
        user.setId(3L);

        when(catalogueRepository.findById(catalogueDto.getParentId())).thenReturn(java.util.Optional.of(parentCatalogue));
        when(userRepository.findById(catalogueDto.getUserCreatedById())).thenReturn(java.util.Optional.of(user));

        // When
        Catalogue catalogue = catalogueParser.DTOtoE(catalogueDto);

        // Then
        assertThat(catalogue.getId()).isEqualTo(catalogueDto.getId());
        assertThat(catalogue.getName()).isEqualTo(catalogueDto.getName());
        assertThat(catalogue.getParentCatalogue()).isEqualTo(parentCatalogue);
        assertThat(catalogue.getUserCreatedBy()).isEqualTo(user);
    }

    @Test
    void testEtoDTO() {
        // Create a Catalogue object to convert to a DTO
        Catalogue catalogue = new Catalogue();
        catalogue.setId(1L);
        catalogue.setCreatedTime(new Date(123456789L));
        catalogue.setName("Catalogue");
        Catalogue parentCatalogue = new Catalogue();
        parentCatalogue.setId(2L);
        catalogue.setParentCatalogue(parentCatalogue);
        User user = new User();
        user.setId(3L);
        catalogue.setUserCreatedBy(user);

        // Call the EtoDTO method on the CatalogueParser object
        CatalogueDto catalogueDto = new CatalogueParser().EtoDTO(catalogue);

        // Check that the DTO has the expected values
        assertEquals(1L, catalogueDto.getId());
        assertEquals(new Timestamp(123456789L), catalogueDto.getCreatedTime());
        assertEquals(2L, catalogueDto.getParentId());
        assertEquals(3L, catalogueDto.getUserCreatedById());
        assertEquals("Catalogue", catalogueDto.getName());
        assertEquals("CATALOGUE", catalogueDto.getTypeOfFile());
    }

}