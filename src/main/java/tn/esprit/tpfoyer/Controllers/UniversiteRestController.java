package tn.esprit.tpfoyer.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer.entities.Foyer;
import tn.esprit.tpfoyer.entities.Universite;
import tn.esprit.tpfoyer.services.IUniversiteService;

@RestController
@AllArgsConstructor
@RequestMapping("/universite")
public class UniversiteRestController {

    IUniversiteService universiteService;

    @Operation(
            summary = "Affecter un foyer à une université",
            description = "Assigne un foyer à une université donnée en fonction de l'ID du foyer et du nom de l'université."
    )
    @ApiResponse(responseCode = "200", description = "Foyer affecté à l'université avec succès")
    @ApiResponse(responseCode = "404", description = "Foyer ou Université non trouvés")
    @PostMapping("/affecterFoyerAUniversite/{idFoyer}/{nomUniversite}")
    @ResponseBody
    public Universite affecterFoyerAUniversite(
            @PathVariable("idFoyer") long idFoyer,
            @PathVariable("nomUniversite") String nomUniversite) {
        return universiteService.affecterFoyerAUniversite(idFoyer, nomUniversite);
    }
}
