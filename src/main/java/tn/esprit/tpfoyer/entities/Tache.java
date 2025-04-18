package tn.esprit.tpfoyer.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table( name = "Tache")
public class Tache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTache;
    private LocalDate dateTache;
    private Integer duree;
    private Float tarifHoraire;

    @ManyToOne
    Etudiant etudiant;

    @OneToOne
    private Etudiant etudiantt;

    @Enumerated(EnumType.STRING)
    private TypeTache typeTache;

    public Tache(Long idTache, LocalDate dateTache, Integer duree, Float tarifHoraire, TypeTache typeTache) {
        this.idTache = idTache;
        this.dateTache = dateTache;
        this.duree = duree;
        this.tarifHoraire = tarifHoraire;
        this.typeTache = typeTache;
    }

    public Tache() {

    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public Long getIdTache() {
        return idTache;
    }

    public void setIdTache(Long idTache) {
        this.idTache = idTache;
    }

    public LocalDate getDateTache() {
        return dateTache;
    }

    public void setDateTache(LocalDate dateTache) {
        this.dateTache = dateTache;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public Float getTarifHoraire() {
        return tarifHoraire;
    }

    public void setTarifHoraire(Float tarifHoraire) {
        this.tarifHoraire = tarifHoraire;
    }

    public TypeTache getTypeTache() {
        return typeTache;
    }

    public void setTypeTache(TypeTache typeTache) {
        this.typeTache = typeTache;
    }

    @Override
    public String toString() {
        return "Tache{" +
                "idTache=" + idTache +
                ", dateTache=" + dateTache +
                ", duree=" + duree +
                ", tarifHoraire=" + tarifHoraire +
                ", typeTache=" + typeTache +
                '}';
    }
}
