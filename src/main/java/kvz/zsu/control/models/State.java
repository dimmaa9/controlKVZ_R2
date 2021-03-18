package kvz.zsu.control.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "state")
@Data
@NoArgsConstructor
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String state;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL)
    private List<Thing> thingList;

    @Override
    public String toString() {
        return state;
    }
}
