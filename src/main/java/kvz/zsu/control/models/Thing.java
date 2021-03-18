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

    @ManyToOne(fetch = FetchType.EAGER)
    private Object object;

    @ManyToOne(fetch = FetchType.LAZY)
    private Unit unit;

    //За штатом
    private Integer generalNeed;

    //В наявності
    private Integer generalHave;

    @Override
    public String toString() {
        return "Thing{" +
                "id=" + id +
                ", object=" + object +
                ", unit=" + unit +
                ", generalNeed=" + generalNeed +
                ", generalHave=" + generalHave +
                '}';
    }
}
