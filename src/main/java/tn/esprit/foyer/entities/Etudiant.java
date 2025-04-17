package tn.esprit.foyer.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RequiredArgsConstructor
public class Etudiant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEtudiant; // Clé primaire

    @NonNull
    private String nomEt;

    @NonNull
     String prenomEt;
     Long cin;
     String ecole;
     LocalDate dateNaissance;


    @OneToMany(mappedBy = "etudiant")
    List<Tache> taches;
    Float montantInscription;
    @Enumerated(EnumType.STRING)
    TypeEtudiant typeEtudiant;

    @ManyToMany(mappedBy = "etudiants",fetch = FetchType.EAGER)
    List<Reservation> reservations;

    @OneToOne
    Tache tache;
    public Etudiant(String nomEt, String prenomEt, String ecole) {
        this.nomEt = nomEt;
        this.prenomEt = prenomEt;
        this.ecole = ecole;
    }


    @ManyToOne
    @JoinColumn(name = "idUniversite") // This will link to Universite
    private Universite universite;  // Association with Universite

    @OneToMany(mappedBy = "etudiant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews;




}

