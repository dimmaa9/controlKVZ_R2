package kvz.zsu.control.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "type")
@Data
@NoArgsConstructor
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private Boolean isCompletType;

    @ManyToOne(fetch = FetchType.EAGER)
    private Scope scope;

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL)
    private List<Object> objectList;

    @Override
    public String toString() {
        return type;
    }
}
