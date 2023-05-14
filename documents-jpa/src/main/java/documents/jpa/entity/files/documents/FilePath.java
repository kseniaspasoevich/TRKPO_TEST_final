package documents.jpa.entity.files.documents;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@ToString(exclude = "parent")
public class FilePath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    @NotNull
    private Long size;
    @NotNull
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_concrete_document")
    @NotNull
    private ConcreteDocument parent;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date createdTime;

}
