package documents.jpa.entity.files.catalogues;

import documents.jpa.entity.files.FileAbstract;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@DiscriminatorValue("CATALOGUE")
@Table(name = "catalogue", uniqueConstraints =
        {
                @UniqueConstraint(columnNames = {"name", "fk_parent"})
        })
public class Catalogue extends FileAbstract {

    @NotNull
    private String name;

    @OneToMany(mappedBy = "parentCatalogue",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE)
    private List<FileAbstract> children = new ArrayList<>();
}
