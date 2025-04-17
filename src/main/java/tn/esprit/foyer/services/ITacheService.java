package tn.esprit.foyer.services;

import tn.esprit.foyer.entities.Etudiant;
import tn.esprit.foyer.entities.Tache;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ITacheService {

    List<Tache> retrieveAllTaches();
    Tache addTache(Tache t);
    Tache updateTache(Tache t);
    Tache retrieveTache(Long idTache);
    void removeTache(Long idTache);

     List<Tache>  addTachesAndAffectToEtudiant (List<Tache> taches, String nomEt, String prenomEt ) ;

    HashMap<String,Float > calculNouveauMontantInscriptionDesEtudiants() ;

    void updateNouveauMontantInscriptionDesEtudiants();
    // --- Signature de la nouvelle méthode de statistiques ---
    Map<String, Object> generateCostReportWithoutStatus(LocalDate startDate, LocalDate endDate);

}
