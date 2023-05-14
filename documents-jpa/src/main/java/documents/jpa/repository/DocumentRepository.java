package documents.jpa.repository;

import documents.jpa.entity.files.documents.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long>,
        PagingAndSortingRepository<Document, Long> {

    @Query(value = "SELECT * FROM Document c WHERE c.fk_parent = ?1 ORDER BY (CASE " +
            "WHEN c.priority = 'HIGH' THEN 1 " +
            "WHEN c.priority = 'DEFAULT' THEN 2 else 3 END)",
            nativeQuery = true)
    List<Document> getChildrens(Long idd);




    @Query(value = "SELECT * FROM Document c ORDER BY (CASE " +
            "WHEN c.priority = 'HIGH' THEN 1 " +
            "WHEN c.priority = 'DEFAULT' THEN 2 else 3 END)",
            nativeQuery = true)
    Page<Document> findAllDocuments(Pageable pageable);

    @Query(value = "SELECT * FROM Document c JOIN concrete_document cd on c.top_version_document_id = cd.id " +
            "WHERE cd.name ILIKE ?1 ORDER BY (CASE " +
            "WHEN c.priority = 'HIGH' THEN 1 " +
            "WHEN c.priority = 'DEFAULT' THEN 2 else 3 END)",
            nativeQuery = true)
    Page<Document> findAllDocumentsByName(String name, Pageable pageable);

    @Query(value = "SELECT * FROM Document c JOIN documenttype d on d.id = c.document_type_id WHERE d.name = ?1 " +
            "ORDER BY (CASE WHEN c.priority = 'HIGH' THEN 1 " +
                    "WHEN c.priority = 'DEFAULT' THEN 2 else 3 END)",
            nativeQuery = true)
    Page<Document> findAllDocumentsByType(String type, Pageable pageable);

    @Query(value = "SELECT * FROM Document c JOIN documenttype d on d.id = c.document_type_id " +
            "JOIN concrete_document cd on c.top_version_document_id = cd.id " +
            "WHERE d.name = ?2 AND " +
            "cd.name ILIKE ?1 " +
            "ORDER BY (CASE WHEN c.priority = 'HIGH' THEN 1 " +
            "WHEN c.priority = 'DEFAULT' THEN 2 else 3 END)",
            nativeQuery = true)
    Page<Document> findAllDocumentsByNameAndType(String name, String type, Pageable pageable);
}
