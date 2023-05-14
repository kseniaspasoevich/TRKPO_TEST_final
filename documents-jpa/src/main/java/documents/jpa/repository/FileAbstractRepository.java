package documents.jpa.repository;

import documents.jpa.entity.files.FileAbstract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileAbstractRepository extends JpaRepository<FileAbstract, Long> {

}
