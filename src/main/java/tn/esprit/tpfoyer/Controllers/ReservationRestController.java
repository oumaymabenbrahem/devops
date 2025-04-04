package tn.esprit.tpfoyer.Controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tn.esprit.tpfoyer.entities.Reservation;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.tpfoyer.services.IReservationService;

@RestController
@AllArgsConstructor
@RequestMapping("/reservations")
public class ReservationRestController {
    IReservationService reservationService;


    @GetMapping("/{id}")
    public Reservation getReservation(@PathVariable Long id) {
        return reservationService.getReservation(id);
    }

}

