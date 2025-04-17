package tn.esprit.foyer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int note; // Note de 1 à 5 ⭐️

    private String commentaire; // Avis texte

    @ManyToOne
    @JsonIgnore
    private Universite universite; // Université concernée

    @ManyToOne
    private Etudiant etudiant; // Etudiant qui laisse l’avis
}

