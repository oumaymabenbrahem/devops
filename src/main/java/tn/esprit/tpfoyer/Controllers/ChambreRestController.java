package tn.esprit.tpfoyer.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer.entities.Chambre;
import tn.esprit.tpfoyer.entities.TypeChambre;
import tn.esprit.tpfoyer.services.ChambreServiceImpl;
import tn.esprit.tpfoyer.services.IChambreService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/chambres")
public class ChambreRestController {

    IChambreService chambreService;

    @Operation(
            summary = "Obtenir des chambres par type et bloc",
            description = "Récupère toutes les chambres d'un type spécifique et dans un bloc donné. Utilisez ce point de terminaison pour filtrer les chambres en fonction du type (par exemple, simple, double, etc.) et du bloc (par exemple, Bloc A)."
    )
    @ApiResponse(responseCode = "200", description = "Liste des chambres récupérées avec succès")
    @ApiResponse(responseCode = "404", description = "Bloc ou type de chambre non trouvé")
    @GetMapping("/typeChambre/{typeChambre}/bloc/{nomBloc}")
    public List<Chambre> getChambresByTypeChambreAndBloc(@PathVariable TypeChambre typeChambre, @PathVariable String nomBloc) {
        return chambreService.findByTypeChambreAndBlocNomBloc(typeChambre, nomBloc);
    }

    @Operation(
            summary = "Obtenir des chambres avec réservations validées",
            description = "Récupère toutes les chambres dont les réservations sont validées ou non, selon le paramètre 'estValide'. Utilisez ce point de terminaison pour filtrer les chambres en fonction de la validation de leur réservation."
    )
    @ApiResponse(responseCode = "200", description = "Liste des chambres récupérées avec succès")
    @ApiResponse(responseCode = "400", description = "Paramètre 'estValide' incorrect, doit être un booléen")
    @GetMapping("/reservations/valides")
    public List<Chambre> getChambresByReservationsValides(@RequestParam boolean estValide) {
        return chambreService.findByReservationsEstValide(estValide);
    }

    @Operation(
            summary = "Obtenir des chambres par bloc et capacité",
            description = "Récupère toutes les chambres dans un bloc donné, avec une capacité supérieure à une valeur spécifiée. Utilisez ce point de terminaison pour filtrer les chambres d'un bloc en fonction de leur capacité."
    )
    @ApiResponse(responseCode = "200", description = "Liste des chambres récupérées avec succès")
    @ApiResponse(responseCode = "404", description = "Bloc non trouvé ou aucune chambre correspondant à la capacité spécifiée")
    @GetMapping("/bloc/{nomBloc}/capacite/{capaciteBloc}")
    public List<Chambre> getChambresByBlocAndCapacite(@PathVariable String nomBloc, @PathVariable long capaciteBloc) {
        return chambreService.findByBlocNomBlocAndBlocCapaciteBlocGreaterThan(nomBloc, capaciteBloc);
    }
}
