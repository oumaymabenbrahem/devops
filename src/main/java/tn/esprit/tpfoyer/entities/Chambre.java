package tn.esprit.tpfoyer.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults (level= AccessLevel.PRIVATE)
@Entity
@ToString
@Builder

public class Chambre  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idFoyer ;
    private long numChambre;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Reservation> reservations;

    @ManyToOne
    Bloc bloc;

    @Enumerated(EnumType.STRING)
    private TypeChambre typeChambre;




    @Override
    public String toString() {
        return "Chambre{" +
                "idFoyer=" + idFoyer +
                ", numChambre=" + numChambre +
                ", TypeChambre=" + typeChambre +
                '}';
    }
}
