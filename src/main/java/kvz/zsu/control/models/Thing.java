package kvz.zsu.control.models;

import jdk.jfr.Timespan;
import jdk.jfr.Timestamp;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Column(name = "create_date")
    @Basic
    private LocalDate localDate;

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
