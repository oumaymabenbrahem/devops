package tn.esprit.foyer.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@Builder // Pratique pour construire l'objet dans le service
@ToString // Ajoutez cette annotation

public class FoyerOccupancyStats {

    private Long foyerId;
    private String nomFoyer;
    private LocalDate periodeAnalyseDebut;
    private LocalDate periodeAnalyseFin;
    private Long capaciteDeclaree; // La valeur dans Foyer.capaciteFoyer
    private long capaciteReelleTotale; // Calculée à partir des chambres
    private long placesOccupees;       // Calculées à partir des réservations valides
    private long placesDisponibles;    // Calculées
    private double tauxOccupation;     // Calculé (%)

}