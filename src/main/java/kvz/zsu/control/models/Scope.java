package kvz.zsu.control.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "scope")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scope {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String scope;

    @OneToMany(mappedBy = "scope", cascade = CascadeType.ALL)
    private List<Type> typeList;

    @Override
    public String toString() {
        return scope;
    }
}
