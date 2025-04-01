package tn.esprit.tpfoyer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.tpfoyer.entities.Chambre;
import tn.esprit.tpfoyer.entities.TypeChambre;
import java.util.List;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Long> {


    List<Chambre> findByBlocNomBloc ( String NomBloc);

    List<Chambre> findByTypeChambreAndBlocNomBloc (TypeChambre typeChambre, String NomBloc);
    List<Chambre> findByReservationsEstValide (boolean estValide);
    List<Chambre> findByBlocNomBlocAndBlocCapaciteBlocGreaterThan (String nomBloc,long capaciteBloc );
    @Query("select c from Chambre c where c.typeChambre=:typeChambre and c.bloc.nomBloc=:nomBloc ")
    List<Chambre> findByTypeCBloc (@Param("typeChambre") TypeChambre typeChambre , @Param("nomBloc ") String nomBloc );
    @Query("select c from Chambre c join c.reservations res where res.estValide=:estValide ")
    List<Chambre> findByValidite (@Param("estValide") boolean estValide  );
    @Query("select c from Chambre c where c.bloc.nomBloc=:nomBloc and c.bloc.capaciteBloc>:capaciteBloc ")
    List<Chambre> findByTypeCBloc (@Param("nomBloc") String nomBloc , @Param("capaciteBloc") long capaciteBloc );
    @Query("SELECT c FROM Chambre c WHERE c.numChambre IN :numChambres")
    List<Chambre> findByNumChambreIn(@Param("numChambres") List<Long> numChambres);
}
