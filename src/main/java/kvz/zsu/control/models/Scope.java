package kvz.zsu.control.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "scope")
@Data
@NoArgsConstructor
public class Scope {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String scope;

    @OneToMany(mappedBy = "scope", cascade = CascadeType.ALL)
    private List<Type> typeList;
}
