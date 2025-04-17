package tn.esprit.foyer.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.foyer.entities.Universite;
import tn.esprit.foyer.services.IUniversiteService;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/universite")
public class UniversiteRestController {
    IUniversiteService universiteService;
    // http://localhost:8089/foyer/universite/retrieve-all-universites
    @GetMapping("/retrieve-all-universites")
    @ResponseBody
    public List<Universite> getUniversites() {
        List<Universite> listUniversites = universiteService.retrieveAllUniversites();
        return listUniversites;
    }

    // http://localhost:8089/foyer/universite/retrieve-universite/8
    @GetMapping("/retrieve-universite/{universiteId}")
    @ResponseBody
    public Universite retrieveUniversite(@PathVariable("universiteId") Long universiteId) {
        return universiteService.retrieveUniversite(universiteId);
    }

    // http://localhost:8089/foyer/universite/add-universite
    @PostMapping("/add-universite")
    @ResponseBody
    public Universite addUniversite(@RequestBody Universite u) {
        Universite universite= universiteService.addUniversite(u);
        return universite;
    }

    // http://localhost:8089/foyer/universite/update-universite
    @PutMapping("/update-universite")
    @ResponseBody
    public Universite updateUniversite(@RequestBody Universite u) {
        Universite universite= universiteService.updateUniversite(u);
        return universite;
    }
    // http://localhost:8089/foyer/universite/removeUniversite
    @DeleteMapping("/removeUniversite/{idUniversite}")
    @ResponseBody
    public void removeUniversite(@PathVariable("idUniversite") Long idUniversite) {
        universiteService.removeUniversite(idUniversite);
    }

    // http://localhost:8089/foyer/universite/affecterFoyerAUniversite/1/esprit
    @PutMapping("/affecterFoyerAUniversite/{idFoyer}/{nomUniversite}")
    @ResponseBody
    public Universite affecterFoyerAUniversite (@PathVariable("idFoyer") long idFoyer,
                                                @PathVariable("nomUniversite") String nomUniversite) {
        return universiteService.affecterFoyerAUniversite(idFoyer,nomUniversite);
    }

    // http://localhost:8089/foyer/universite/desaffecterFoyerAUniversite/1/1
    @PutMapping("/desaffecterFoyerAUniversite/{idFoyer}/{idUniversite}")
    @ResponseBody
    public Universite desaffecterFoyerAUniversite (@PathVariable("idFoyer") long idFoyer) {
        universiteService.desaffecterFoyerAUniversite(idFoyer);
        return null;
    }
    // ✅ Affectation intelligente d'un étudiant à la meilleure université disponible
    @PutMapping("/affecterMeilleureUniversite/{idEtudiant}")
    public ResponseEntity<Universite> affecterMeilleureUniversite(@PathVariable Long idEtudiant) {
        try {
            // Call the service method to affect the best university to the student
            Universite universite = universiteService.affecterEtudiantMeilleureUniversite(idEtudiant);

            // Return the best university in the response with HTTP status OK
            return new ResponseEntity<>(universite, HttpStatus.OK);
        } catch (RuntimeException e) {
            // If an error occurs (e.g., student not found or no universities found), return a BAD_REQUEST
            // or any other appropriate status code, along with the error message.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // or BAD_REQUEST, depending on your needs
        }
    }


    // ✅ Ajouter un avis et recalculer la réputation
    @PostMapping("/ajouterAvis/{idUniversite}/{idEtudiant}/{note}")
    public ResponseEntity<Universite> ajouterAvis(
            @PathVariable Long idUniversite,
            @PathVariable Long idEtudiant,
            @PathVariable int note,
            @RequestBody String commentaire) {
        try {
            Universite universite = universiteService.ajouterAvis(idUniversite, idEtudiant, note, commentaire);
            return new ResponseEntity<>(universite, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // ✅ Récupérer le classement des universités selon leur score
    @GetMapping("/classement")
    public List<Universite> getClassementUniversites() {
        return universiteService.getClassementUniversites();
    }



}
