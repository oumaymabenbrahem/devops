package tn.esprit.tpfoyer.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table( name = "Reservation")
public class Reservation
{
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long idReservation;
private Date anneeUniversitaire;
private boolean estValide;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Etudiant> etudiants;

    public Reservation(long idReservation, Date anneeUniversitaire, boolean estValide) {
        this.idReservation = idReservation;
        this.anneeUniversitaire = anneeUniversitaire;
        this.estValide = estValide;
    }

    public Reservation() {

    }

    public long getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(long idReservation) {
        this.idReservation = idReservation;
    }

    public Date getAnneeUniversitaire() {
        return anneeUniversitaire;
    }

    public void setAnneeUniversitaire(Date anneeUniversitaire) {
        this.anneeUniversitaire = anneeUniversitaire;
    }

    public boolean isEstValide() {
        return estValide;
    }

    public void setEstValide(boolean estValide) {
        this.estValide = estValide;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "idReservation=" + idReservation +
                ", anneeUniversitaire=" + anneeUniversitaire +
                ", estValide=" + estValide +
                '}';
    }
}
