package tn.esprit.tpfoyer.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer.entities.Tache;
import tn.esprit.tpfoyer.services.ITacheService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/taches") // Mappage de base pour la gestion des tâches
public class TacheRestController {

    ITacheService tacheService;

    @Operation(
            summary = "Récupérer toutes les tâches",
            description = "Récupère la liste complète de toutes les tâches existantes."
    )
    @ApiResponse(responseCode = "200", description = "Liste des tâches récupérée avec succès")
    @ApiResponse(responseCode = "404", description = "Aucune tâche trouvée")
    @GetMapping("/retrieve-all-taches")
    public List<Tache> getTaches() {
        return tacheService.retrieAllTaches();
    }

    @Operation(
            summary = "Ajouter une tâche",
            description = "Crée une nouvelle tâche et l'ajoute à la base de données."
    )
    @ApiResponse(responseCode = "200", description = "Tâche ajoutée avec succès")
    @ApiResponse(responseCode = "400", description = "Données de la tâche invalides")
    @PostMapping("/add-tache")
    public Tache addTache(@RequestBody Tache tache) {
        return tacheService.addTache(tache);
    }

    @Operation(
            summary = "Mettre à jour une tâche",
            description = "Met à jour une tâche existante en fonction des informations envoyées."
    )
    @ApiResponse(responseCode = "200", description = "Tâche mise à jour avec succès")
    @ApiResponse(responseCode = "404", description = "Tâche non trouvée")
    @PutMapping("/update-tache")
    public Tache updateTache(@RequestBody Tache e) {
        return tacheService.updateTache(e);
    }

    @Operation(
            summary = "Supprimer une tâche",
            description = "Supprime une tâche existante de la base de données en fonction de son identifiant."
    )
    @ApiResponse(responseCode = "200", description = "Tâche supprimée avec succès")
    @ApiResponse(responseCode = "404", description = "Tâche non trouvée")
    @DeleteMapping("/delete-tache/{id}")
    public void deleteTache(@PathVariable Long id) {
        tacheService.removeTache(id);
    }

    @Operation(
            summary = "Ajouter des tâches et les affecter à un étudiant",
            description = "Ajoute plusieurs tâches et les affecte à un étudiant spécifié par son nom et prénom."
    )
    @ApiResponse(responseCode = "200", description = "Tâches ajoutées et affectées à l'étudiant avec succès")
    @ApiResponse(responseCode = "404", description = "Étudiant non trouvé")
    @PostMapping("/addTasksAndAssignToEtudiant")
    public List<Tache> addTasksAndAffectToEtudiant(@RequestBody List<Tache> tasks,
                                                   @RequestParam String nomEt,
                                                   @RequestParam String prenomEt) {
        return tacheService.addTasksAndAffectToEtudiant(tasks, nomEt, prenomEt);
    }
}
