package documents.jpa.repository;

import documents.jpa.entity.files.documents.ConcreteDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ConcreteDocumentRepository extends JpaRepository<ConcreteDocument, Long> {

    @Query(value = "SELECT c FROM ConcreteDocument c INNER JOIN c.parent cc WHERE cc.id = ?1")
    List<ConcreteDocument> getAllVersions(@Param("idd") Long idd);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM ConcreteDocument c WHERE c.parent in (SELECT d from Document d where d.id = ?1)")
    void deleteByParent_id(@Param("idd") Long idd);

}
