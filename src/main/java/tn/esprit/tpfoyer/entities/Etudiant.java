package tn.esprit.tpfoyer.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table( name = "Etudiant")
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEtudiant;
    private String nomEt;
    private String prenomEt;
    private Long cin;
    private String ecole;
    private Date dateNaissance;



    @OneToMany(cascade = CascadeType.ALL, mappedBy="etudiant")
    private Set<Tache> taches;


    @ManyToMany(mappedBy="etudiants", cascade = CascadeType.ALL)
    private Set<Reservation> reservations;


    @OneToOne(mappedBy="etudiantt")
    private Tache tache;


    @Enumerated(EnumType.STRING)
    private TypeEtudiant typeEtudiant;










}
