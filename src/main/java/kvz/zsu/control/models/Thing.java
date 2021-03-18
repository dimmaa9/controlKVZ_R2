package kvz.zsu.control.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "thing")
@Data
@NoArgsConstructor
public class Thing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;
    private Integer category;

    @ManyToOne(fetch = FetchType.EAGER)
    private Object object;

    @ManyToOne(fetch = FetchType.EAGER)
    private State state;

    //Вложеность
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Thing parentThing;
    @OneToMany(mappedBy="parentThing", cascade = CascadeType.ALL)
    private List<Thing> things = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Unit unit;

    @Override
    public String toString() {
        return "Thing{" +
                "id=" + id +
                ", price=" + price +
                ", category=" + category +
                ", state=" + state +
                ", unit=" + unit +
                '}';
    }
}
