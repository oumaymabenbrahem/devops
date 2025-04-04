package tn.esprit.tpfoyer.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.tpfoyer.services.IFoyerService;

@RestController
@AllArgsConstructor
@RequestMapping("/foyer")

public class FoyerRestController {
    IFoyerService  foyerService;



}
