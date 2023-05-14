package documents.jpa.entity.files.documents;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "documenttype")
public class DocumentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public DocumentType(String name) {
        this.name = name;
    }

    public DocumentType(long l, String name) {
        this.name = name;
    }
}
