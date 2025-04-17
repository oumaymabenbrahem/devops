package tn.esprit.foyer.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Universite implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idUniversite")
    Long idUniversite; // Clé primaire
    String nomUniversite;
    String adresse;
    @OneToOne(mappedBy = "universite",cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
    Foyer foyer;
    //shayma
    @Column(nullable = false)
    private double reputationScore = 0.0; // Score de réputation initial
    @OneToMany(mappedBy = "universite", cascade = CascadeType.ALL)
    private List<Review> reviews; // Liste des avis donnés par les étudiants

}

