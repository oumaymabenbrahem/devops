package tn.esprit.tpfoyer.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer.entities.Bloc;
import tn.esprit.tpfoyer.services.IBlocService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/blocs")
public class BlocRestController {

    IBlocService blocService;

    @Operation(
            summary = "Lister les blocs selon l'université",
            description = "Récupère tous les blocs associés à une université donnée. Utilisez ce point de terminaison pour obtenir la liste des blocs en fonction du nom de l'université."
    )
    @ApiResponse(responseCode = "200", description = "Liste des blocs récupérée avec succès")
    @ApiResponse(responseCode = "404", description = "Université non trouvée")
    @GetMapping("/universite/{nomUniversite}")
    public List<Bloc> getBlocsByUniversiteName(@PathVariable String nomUniversite) {
        return blocService.findByFoyerUniversiteNomUniversite(nomUniversite);
    }

    @Operation(
            summary = "Affecter des chambres à un bloc",
            description = "Assigne une liste de chambres à un bloc donné. Utilisez ce point de terminaison pour associer un ou plusieurs numéros de chambres à un bloc spécifique."
    )
    @ApiResponse(responseCode = "200", description = "Chambres affectées au bloc avec succès")
    @ApiResponse(responseCode = "404", description = "Bloc non trouvé ou erreurs d'affectation")
    @ApiResponse(responseCode = "400", description = "Liste des numéros de chambres invalide ou format incorrect")
    @PostMapping("/affecterChambresABloc/{nomBloc}")
    @ResponseBody
    public Bloc affecterChambresABloc(
            @PathVariable("nomBloc") String nomBloc,
            @RequestBody List<Long> numChambres) {
        return blocService.affecterChambresABloc(numChambres, nomBloc);
    }
}
