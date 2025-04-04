package tn.esprit.tpfoyer.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idReservation;

    private Date anneeUniversitaire;
    private boolean estValide;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Etudiant> etudiants;

    @ManyToOne // Ajout de la relation avec Chambre
    private Chambre chambre;

    // Constructeur avec Chambre et Etudiant
    public Reservation(Chambre chambre, Etudiant etudiant) {
        this.chambre = chambre;
        this.etudiants = Set.of(etudiant);
        this.estValide = false; // Par défaut, la réservation n'est pas encore validée
    }

    // Constructeur vide
    public Reservation() {}

    // Getters et Setters
    public long getIdReservation() { return idReservation; }
    public void setIdReservation(long idReservation) { this.idReservation = idReservation; }



    public Date getAnneeUniversitaire() { return anneeUniversitaire; }
    public void setAnneeUniversitaire(Date anneeUniversitaire) { this.anneeUniversitaire = anneeUniversitaire; }

    public boolean isEstValide() { return estValide; }
    public void setEstValide(boolean estValide) { this.estValide = estValide; }

    public Chambre getChambre() { return chambre; }
    public void setChambre(Chambre chambre) { this.chambre = chambre; }

    public Set<Etudiant> getEtudiants() { return etudiants; }
    public void setEtudiants(Set<Etudiant> etudiants) { this.etudiants = etudiants; }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", anneeUniversitaire=" + anneeUniversitaire +
                ", estValide=" + estValide +
                ", chambre=" + (chambre != null ? chambre.getNumChambre() : "null") +
                '}';
    }
}

