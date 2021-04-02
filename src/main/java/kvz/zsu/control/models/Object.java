package kvz.zsu.control.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "object")
@Data
@NoArgsConstructor
public class Object {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String objectName;

    private Double price;

    @ManyToOne(fetch = FetchType.EAGER)
    private Type type;


    @OneToMany(mappedBy = "object", cascade = CascadeType.ALL)
    private List<Thing> thingList;

    @Override
    public String toString() {
        return objectName;
    }
}
