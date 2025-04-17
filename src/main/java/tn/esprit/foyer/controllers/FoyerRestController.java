package tn.esprit.foyer.controllers;


import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.foyer.entities.Foyer;
import tn.esprit.foyer.entities.FoyerOccupancyStats;
import tn.esprit.foyer.services.IFoyerService;

import java.time.LocalDate;
import java.util.List;
@RestController
@AllArgsConstructor
@RequestMapping("/foyer")

public class FoyerRestController {

    IFoyerService foyerService;
    // http://localhost:8089/foyer/foyer/retrieve-all-foyers
    @GetMapping("/retrieve-all-foyers")
    @ResponseBody
    public List<Foyer> getFoyers() {
        List<Foyer> listFoyers = foyerService.retrieveAllFoyers();
        return listFoyers;
    }

    // http://localhost:8089/foyer/foyer/retrieve-foyer/8
    @GetMapping("/retrieve-foyer/{foyerId}")
    @ResponseBody
    public Foyer retrieveFoyer(@PathVariable("foyerId") Long foyerId) {
        return foyerService.retrieveFoyer(foyerId);
    }

    // http://localhost:8089/foyer/foyer/add-foyer
    @PostMapping("/add-foyer")
    @ResponseBody
    public Foyer addFoyer(@RequestBody Foyer f) {
        Foyer foyer= foyerService.addFoyer(f);
        return foyer;
    }

    // http://localhost:8089/foyer/foyer/add-foyer-with-bloc
    @PostMapping("/add-foyer-with-bloc")
    @ResponseBody
    public Foyer addFoyerWithBloc(@RequestBody Foyer f) {
        Foyer foyer= foyerService.addFoyerWithBloc(f);
        return foyer;
    }

    // http://localhost:8089/foyer/foyer/update-foyer
    @PutMapping("/update-foyer")
    @ResponseBody
    public Foyer updateFoyer(@RequestBody Foyer f) {
        Foyer foyer= foyerService.updateFoyer(f);
        return foyer;
    }
    // http://localhost:8089/foyer/foyer/removeFoyer
    @DeleteMapping("/removeFoyer/{idFoyer}")
    @ResponseBody
    public void removeFoyer(@PathVariable("idFoyer") Long idFoyer) {
        foyerService.removeFoyer(idFoyer);
    }
    @GetMapping("/{foyerId}/statistiques/occupation")
    public ResponseEntity<FoyerOccupancyStats> getFoyerOccupancyStatistics(
            @PathVariable("foyerId") Long foyerId,
            @RequestParam(value = "debut", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam(value = "fin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        // La gestion de l'exception EntityNotFoundExceptionById peut être faite
        // globalement avec @ControllerAdvice ou ici avec un try-catch si vous préférez.
        // Ici, on laisse l'exception remonter (si non gérée globalement, elle donnera une 500 par défaut,
        // mais EntityNotFoundExceptionById est annotée avec @ResponseStatus(HttpStatus.NOT_FOUND))
        FoyerOccupancyStats stats = foyerService.getFoyerOccupancyStats(foyerId, debut, fin);
        return ResponseEntity.ok(stats);
    }
}
