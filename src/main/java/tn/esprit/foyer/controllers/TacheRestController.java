package tn.esprit.foyer.controllers;


import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.foyer.entities.Etudiant;
import tn.esprit.foyer.entities.Tache;
import tn.esprit.foyer.services.ITacheService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/tache")

public class TacheRestController {

    ITacheService tacheService;
    // http://localhost:8089/foyer/tache/retrieve-all-taches
    @GetMapping("/retrieve-all-taches")
    @ResponseBody
    public List<Tache> getFoyers() {
        List<Tache> listTaches = tacheService.retrieveAllTaches();
        return listTaches;
    }

    // http://localhost:8089/foyer/tache/retrieve-tache/8
    @GetMapping("/retrieve-tache/{tacheId}")
    @ResponseBody
    public Tache retrieveTache(@PathVariable("tacheId") Long tacheId) {
        return tacheService.retrieveTache(tacheId);
    }

    // http://localhost:8089/foyer/tache/add-tache
    @PostMapping("/add-tache")
    @ResponseBody
    public Tache addTache(@RequestBody Tache t) {
        Tache tache= tacheService.addTache(t);
        return tache;
    }


    // http://localhost:8089/foyer/tache/update-tache
    @PutMapping("/update-tache")
    @ResponseBody
    public Tache updateTache(@RequestBody Tache t) {
        Tache tache= tacheService.updateTache(t);
        return tache;
    }
    // http://localhost:8089/foyer/tache/removeidTache
    @DeleteMapping("/removeTache/{idTache}")
    @ResponseBody
    public void removeTache(@PathVariable("idTache") Long idTache) {
        tacheService.removeTache(idTache);
    }

    // http://localhost:8089/foyer/tache/addTachesAndAffectToEtudiant
    @PostMapping("/addTachesAndAffectToEtudiant/{nomEt}/{prenomEt}")
    @ResponseBody
    public List<Tache> addTachesAndAffectToEtudiant(@RequestBody List<Tache> taches, @PathVariable("nomEt") String nomEt, @PathVariable("prenomEt") String prenomEt) {
        return tacheService.addTachesAndAffectToEtudiant(taches, nomEt, prenomEt);
    }

    // http://localhost:8089/foyer/tache/calculNouveauMontantInscriptionDesEtudiants
    @GetMapping("/calculNouveauMontantInscriptionDesEtudiants")
    public HashMap<String, Float> calculNouveauMontantInscriptionDesEtudiants() {
      return  tacheService.calculNouveauMontantInscriptionDesEtudiants();
    }
    // --- Nouvel Endpoint pour les statistiques de coûts (sans DTO, sans statut) ---

    /**
     * Endpoint pour obtenir le rapport d'analyse des coûts des tâches ASSIGNEES.
     * @param startDate Date de début de la période (format YYYY-MM-DD)
     * @param endDate Date de fin de la période (format YYYY-MM-DD)
     * @return ResponseEntity contenant une Map<String, Object> avec les statistiques
     */
    // Exemple d'appel: http://localhost:8089/foyer/tache/statistiques/couts-assignees?debut=2023-01-01&fin=2023-12-31
    @GetMapping("/statistiques/couts-assignees")
    public ResponseEntity<Map<String, Object>> getAssignedTasksCostAnalysisReport(
            @RequestParam("debut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Dates invalides ou manquantes.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Appelle la méthode de service via l'interface
        Map<String, Object> report = tacheService.generateCostReportWithoutStatus(startDate, endDate);
        return ResponseEntity.ok(report);
    }


}
