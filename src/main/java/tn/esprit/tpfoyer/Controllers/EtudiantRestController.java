package tn.esprit.tpfoyer.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer.entities.Etudiant;
import tn.esprit.tpfoyer.services.IEtudiantService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/etudiant")
@CrossOrigin("*")
public class EtudiantRestController {

    IEtudiantService etudiantService;

    @Operation(
            summary = "Récupérer tous les étudiants",
            description = "Retourne la liste complète des étudiants enregistrés dans la base de données."
    )
    @ApiResponse(responseCode = "200", description = "Liste des étudiants récupérée avec succès")
    @GetMapping("retrieve-all-etudiants")
    public List<Etudiant> getEtudiants() {
        return etudiantService.retrieAllEtudiants();
    }

    @Operation(
            summary = "Ajouter un étudiant",
            description = "Ajoute un nouvel étudiant dans la base de données."
    )
    @ApiResponse(responseCode = "200", description = "Étudiant ajouté avec succès")
    @ApiResponse(responseCode = "400", description = "Les données envoyées sont invalides")
    @PostMapping("add-etudiant")
    public Etudiant addEtudiant(@RequestBody Etudiant etudiant) {
        return etudiantService.addCEtudiant(etudiant);
    }

    @Operation(
            summary = "Mettre à jour un étudiant",
            description = "Met à jour les informations d'un étudiant existant."
    )
    @ApiResponse(responseCode = "200", description = "Étudiant mis à jour avec succès")
    @ApiResponse(responseCode = "404", description = "Étudiant non trouvé")
    @PutMapping("/update-etudiant")
    public Etudiant updateEtudiant(@RequestBody Etudiant etudiant) {
        return etudiantService.updateEtudiant(etudiant);
    }

    @Operation(
            summary = "Supprimer un étudiant",
            description = "Supprime un étudiant de la base de données en fonction de son ID."
    )
    @ApiResponse(responseCode = "200", description = "Étudiant supprimé avec succès")
    @ApiResponse(responseCode = "404", description = "Étudiant non trouvé")
    @DeleteMapping("/delete-etudiant/{id}")
    public void deleteEtudiant(@PathVariable Long id) {
        etudiantService.removeEtudiant(id);
    }
}
