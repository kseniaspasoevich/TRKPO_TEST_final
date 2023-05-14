package documents.jpa.repository;

import documents.jpa.entity.files.catalogues.Catalogue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CatalogueRepository extends JpaRepository<Catalogue, Long>,
        PagingAndSortingRepository<Catalogue, Long> {

    @Query(value = "SELECT c FROM Catalogue c WHERE c.parentCatalogue is null ")
    Optional<Catalogue> getRoot();


    @Query(value = "SELECT * FROM Catalogue c WHERE c.fk_parent = ?1",
    nativeQuery = true)
    List<Catalogue> getChildrens(Long idd);

    List<Catalogue> findAll();

    @Query(value = "SELECT * FROM Catalogue c WHERE c.name ILIKE ?1 ORDER BY name",
            nativeQuery = true)
    Page<Catalogue> findAllCataloguesByName(String name, Pageable pageable);

}
