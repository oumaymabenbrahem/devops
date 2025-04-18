package tn.esprit.tpfoyer.services;

import tn.esprit.tpfoyer.entities.Bloc;
import tn.esprit.tpfoyer.entities.Chambre;
import tn.esprit.tpfoyer.entities.TypeChambre;

import java.util.List;

public interface IChambreService {
    List<Chambre> retrieAllChambres();
    Chambre addChambre(Chambre c);
    Chambre updateChambre(Chambre c);
    Chambre retrieChambre(Long idChambre);
    void removeChambre(Long idChambre);

    List<Chambre> findByTypeChambreAndBlocNomBloc(TypeChambre typeChambre, String nomBloc);

    List<Chambre> findByReservationsEstValide(boolean estValide);

    List<Chambre> findByBlocNomBlocAndBlocCapaciteBlocGreaterThan(String nomBloc, long capaciteBloc);
}
