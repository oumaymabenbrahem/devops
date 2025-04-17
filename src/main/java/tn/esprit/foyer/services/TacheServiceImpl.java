package tn.esprit.foyer.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.foyer.entities.Etudiant;
import tn.esprit.foyer.entities.Tache;
import tn.esprit.foyer.entities.TypeTache;
import tn.esprit.foyer.repository.EtudiantRepository;
import tn.esprit.foyer.repository.TacheRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class TacheServiceImpl implements ITacheService{

    TacheRepository tacheRepository;
    EtudiantRepository etudiantRepository;
    @Override
    public List<Tache> retrieveAllTaches() {
        return tacheRepository.findAll();
    }

    @Override
    public Tache addTache(Tache t) {
        return tacheRepository.save(t);
    }

    @Override
    public Tache updateTache(Tache t) {
        return tacheRepository.save(t);
    }

    @Override
    public Tache retrieveTache(Long idTache) {
        return tacheRepository.findById(idTache).get();
    }

    @Override
    public void removeTache(Long idTache) {
        tacheRepository.deleteById(idTache);
    }




    @Override
    public List<Tache> addTachesAndAffectToEtudiant(List<Tache> taches, String nomEt, String prenomEt) {
        Etudiant et = etudiantRepository.findByNomEtAndPrenomEt(nomEt,prenomEt);
        taches.forEach(tache -> {
            tache.setEtudiant(et);
          //  tacheRepository.save(tache);
        });
        tacheRepository.saveAll(taches);
        return taches;
    }

    @Override
    public HashMap<String, Float> calculNouveauMontantInscriptionDesEtudiants() {
        HashMap<String, Float> nouveauxMontantsInscription = new HashMap<>();
        etudiantRepository.findAll().forEach(etudiant -> {
            Float ancienMontant= etudiant.getMontantInscription();
            LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1,1);
            LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 12,31);
            Float montantTachesAssignesAnneeEnCours = tacheRepository.
                    sommeTacheAnneeEncours(startDate,endDate,etudiant.getIdEtudiant());
            Float nouveauMontant = ancienMontant;
            if (montantTachesAssignesAnneeEnCours!=null) {
                 nouveauMontant = ancienMontant - montantTachesAssignesAnneeEnCours;
            }
            nouveauxMontantsInscription.put(etudiant.getNomEt()+" "+etudiant.getPrenomEt(),
                    nouveauMontant);
        });
        return nouveauxMontantsInscription;
    }

    //@Scheduled(cron = "0 30 14 09 09 *")
    public void updateNouveauMontantInscriptionDesEtudiants() {
        etudiantRepository.findAll().forEach(etudiant -> {
            Float montantInscription= etudiant.getMontantInscription();
            LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1,1);
            LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 12,31);
            Float montantTachesAssignesAnneeEnCours = tacheRepository.sommeTacheAnneeEncours(startDate,endDate,etudiant.getIdEtudiant());
            if (montantTachesAssignesAnneeEnCours!=null) {
                montantInscription = montantInscription - montantTachesAssignesAnneeEnCours;
                etudiant.setMontantInscription(montantInscription);
                etudiantRepository.save(etudiant);
            }

        });
    }


    // --- Nouvelle méthode pour générer le rapport de coûts (sans DTO) ---
    public Map<String, Object> generateCostReportWithoutStatus(LocalDate startDate, LocalDate endDate) {
        log.info("Génération du rapport de coûts (sans statut) pour la période du {} au {}", startDate, endDate);

        Map<String, Object> reportMap = new HashMap<>(); // La map résultat

        reportMap.put("startDate", startDate.toString());
        reportMap.put("endDate", endDate.toString());

        // 1. Calculer le coût total des tâches assignées
        Double totalCost = tacheRepository.calculateTotalCostAssignedTasks(startDate, endDate);
        reportMap.put("totalCostAssignedTasks", totalCost); // Clé modifiée

        // 2. Compter les tâches assignées
        long countAssigned = tacheRepository.countAssignedTasks(startDate, endDate);
        reportMap.put("numberOfAssignedTasks", countAssigned); // Clé modifiée

        // 3. Calculer le coût moyen des tâches assignées
        Double averageCost = 0.0;
        if (countAssigned > 0) {
            averageCost = tacheRepository.calculateAverageCostAssignedTasks(startDate, endDate)
                    .orElse(0.0);
        }
        reportMap.put("averageCostAssignedTasks", averageCost); // Clé modifiée

        // 4. Obtenir et traiter la répartition par type (tâches assignées)
        List<Object[]> rawCostByType = tacheRepository.calculateTotalCostAssignedTasksByTypeRaw(startDate, endDate);
        List<Map<String, Object>> costDistributionByType = new ArrayList<>();
        for (Object[] row : rawCostByType) {
            Map<String, Object> typeCostMap = new HashMap<>();
            typeCostMap.put("typeTache", (TypeTache) row[0]);
            typeCostMap.put("totalCost", (Double) row[1]);
            costDistributionByType.add(typeCostMap);
        }
        reportMap.put("costDistributionByType", costDistributionByType);

        // 5. Obtenir et traiter la répartition par bloc (tâches assignées)
        List<Object[]> rawCostByBloc = tacheRepository.calculateTotalCostAssignedTasksByBlocRaw(startDate, endDate);
        List<Map<String, Object>> costDistributionByBloc = new ArrayList<>();
        for (Object[] row : rawCostByBloc) {
            Map<String, Object> blocCostMap = new HashMap<>();
            blocCostMap.put("nomBloc", row[0] != null ? (String) row[0] : "Non Spécifié/Autre");
            blocCostMap.put("totalCost", (Double) row[1]);
            costDistributionByBloc.add(blocCostMap);
        }
        reportMap.put("costDistributionByBloc", costDistributionByBloc);

        // 6. Calculer les réductions potentielles (basées sur le coût total des tâches assignées)
        Double potentialReductions = totalCost; // La logique de réduction actuelle semble utiliser ce coût
        reportMap.put("potentialReductionsBasedOnAssigned", potentialReductions); // Clé modifiée

        log.info("Rapport de coûts (sans statut) généré avec succès.");
        return reportMap;
    }

}
