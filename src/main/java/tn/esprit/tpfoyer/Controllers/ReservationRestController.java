package tn.esprit.tpfoyer.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.tpfoyer.services.IReservationService;

@RestController
@AllArgsConstructor
public class ReservationRestController {
    IReservationService reservationService;
}
