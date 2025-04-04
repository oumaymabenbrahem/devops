package tn.esprit.tpfoyer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.tpfoyer.entities.Foyer;
import tn.esprit.tpfoyer.entities.Reservation;
import java.util.List;

@Repository
public interface FoyerRepository extends JpaRepository<Foyer, Long> {
List<Foyer> findByUniversiteNomUniversite(String nom);


}
