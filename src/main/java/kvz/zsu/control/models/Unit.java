package kvz.zsu.control.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "unit")
@Data
@NoArgsConstructor
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameUnit;

    //Вложеность
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Unit parentUnit;
    @OneToMany(mappedBy="parentUnit", cascade = CascadeType.ALL)
    private List<Unit> units = new ArrayList<>();

    @OneToMany(mappedBy = "unit", fetch = FetchType.EAGER)
    private List<Thing> thingList;

    @OneToMany(mappedBy="unit", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

    @Override
    public String toString() {
        return nameUnit;
    }
}
