package tn.esprit.tpfoyer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.tpfoyer.entities.Tache;


@Repository
public interface TacheRepository extends JpaRepository<Tache, Long> {

}
