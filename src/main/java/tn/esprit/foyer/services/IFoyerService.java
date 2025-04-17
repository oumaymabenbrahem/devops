package tn.esprit.foyer.services;

import tn.esprit.foyer.entities.Foyer;
import tn.esprit.foyer.entities.FoyerOccupancyStats;

import java.time.LocalDate;
import java.util.List;

public interface IFoyerService {

    List<Foyer> retrieveAllFoyers();
    Foyer addFoyer(Foyer f);
    Foyer updateFoyer(Foyer f);
    Foyer retrieveFoyer(Long idFoyer);
    void removeFoyer(Long idFoyer);



    Foyer addFoyerWithBloc(Foyer f);
    FoyerOccupancyStats getFoyerOccupancyStats(Long foyerId, LocalDate dateDebut, LocalDate dateFin);
}
