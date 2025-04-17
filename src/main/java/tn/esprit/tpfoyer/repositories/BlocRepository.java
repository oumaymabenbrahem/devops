package tn.esprit.tpfoyer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.tpfoyer.entities.Bloc;
import tn.esprit.tpfoyer.entities.Universite;

import java.util.List;


@Repository
public interface BlocRepository extends JpaRepository<Bloc, Long> {
    List<Bloc> findByFoyerUniversiteNomUniversite (String nomUniversite);

    @Query("select b from Bloc b where b.foyer.universite.nomUniversite =:nomUniversite ")
    List<Bloc> findfoyerUnivNomUniv ( @Param("nomUniversite") String nomUniversite);

    Bloc findByNomBloc (String nomBloc);

}

