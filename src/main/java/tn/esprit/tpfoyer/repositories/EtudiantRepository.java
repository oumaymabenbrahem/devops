package tn.esprit.tpfoyer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.tpfoyer.entities.Etudiant;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    @Query("SELECT e FROM Etudiant e WHERE e.nomEt = :nomEtudiant AND e.prenomEt = :prenomEtudiant")
    List<Etudiant> findByNomAndPrenom(@Param("nomEtudiant") String nomEtudiant, @Param("prenomEtudiant") String prenomEtudiant);


}
