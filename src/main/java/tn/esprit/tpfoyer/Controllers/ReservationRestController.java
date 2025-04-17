package tn.esprit.tpfoyer.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.tpfoyer.entities.Reservation;
import tn.esprit.tpfoyer.services.IReservationService;
import tn.esprit.tpfoyer.services.ReservationServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@AllArgsConstructor
public class ReservationRestController {
    ReservationServiceImpl reservationService;
    @PutMapping("/expire-old")
    public List<Reservation> expireOldReservations() {
        return reservationService.expireOldReservations();
    }
}
