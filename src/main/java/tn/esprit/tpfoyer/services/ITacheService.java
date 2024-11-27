package tn.esprit.tpfoyer.services;

import tn.esprit.tpfoyer.entities.Foyer;
import tn.esprit.tpfoyer.entities.Tache;

import java.util.List;

public interface ITacheService {
    List<Tache> retrieAllTaches();
    Tache addTache(Tache t);
    Tache updateTache(Tache t);
    Tache retrieTache(Long idTache);
    void removeTache(Long idTache);

    List<Tache> addTasksAndAffectToEtudiant(List<Tache> tasks, String nomEt, String prenomEt);
}
