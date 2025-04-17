package tn.esprit.tpfoyer.services;

import tn.esprit.tpfoyer.entities.Bloc;
import tn.esprit.tpfoyer.entities.Tache;

import java.util.List;

public interface IBlocService {
    List<Bloc> retrieAllBlocs();
    Bloc addBloc(Bloc b);
    Bloc updateBloc(Bloc b);
    Bloc retrieBloc(Long idBloc);
    void removeBloc(Long idBloc);

    List<Bloc> findByFoyerUniversiteNomUniversite(String nomUniversite);

    Bloc affecterChambresABloc(List<Long> numChambre, String nomBloc);
}
