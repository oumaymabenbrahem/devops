package tn.esprit.foyer.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.foyer.entities.Reservation;
import tn.esprit.foyer.services.IReservationService;
import tn.esprit.foyer.services.ReservationServicImpl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
public class ReservationRestController {
    IReservationService reservationService;
    ReservationServicImpl reservationServicImpl;
    // http://localhost:8089/foyer/reservation/retrieve-all-reservations
    @GetMapping("/retrieve-all-reservations")
    @ResponseBody
    public List<Reservation> getReservations() {
        List<Reservation> listReservations = reservationService.retrieveAllReservations();
        return listReservations;
    }

    // http://localhost:8089/foyer/reservation/retrieve-reservation/8
    @GetMapping("/retrieve-reservation/{reservationId}")
    @ResponseBody
    public Reservation retrieveReservation(@PathVariable("reservationId") String reservationId) {
        return reservationService.retrieveReservation(reservationId);
    }

    // http://localhost:8089/foyer/reservation/add-reservation
    @PostMapping("/add-reservation")
    @ResponseBody
    public Reservation addReservation(@RequestBody Reservation r) {
        Reservation reservation= reservationService.addReservation(r);
        return reservation;
    }

    // http://localhost:8089/foyer/reservation/update-reservation
    @PutMapping("/update-reservation")
    @ResponseBody
    public Reservation updateReservation(@RequestBody Reservation r) {
        Reservation reservation= reservationService.updateReservation(r);
        return reservation;
    }
    // http://localhost:8089/foyer/reservation/removeReservation
    @DeleteMapping("/removeReservation/{idReservation}")
    @ResponseBody
    public void removeReservation(@PathVariable("idReservation") String idReservation) {
        reservationService.removeReservation(idReservation);
    }

         // http://localhost:8089/foyer/reservation/ajouterReservationEtAssignerAChambreEtAEtudiant/15/8453621
         @PostMapping("/ajouterReservationEtAssignerAChambreEtAEtudiant/{numChambre}/{cin}")
         @ResponseBody
         public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(@RequestBody Reservation r,@PathVariable("numChambre") Long numChambre,@PathVariable("cin") long cin) {
             Reservation reservation= reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(r,numChambre,cin);
             return reservation;
         }
    // http://localhost:8089/foyer/reservation/getReservationParAnneeUniversitaire/2021-01-01/2021-12-31
    @GetMapping("/getReservationParAnneeUniversitaire/{dateDebut}/{dateFin}")
    @ResponseBody
    public List<Reservation> getReservationParAnneeUniversitaire(@PathVariable("dateDebut") LocalDate dateDebut,@PathVariable("dateFin") LocalDate dateFin) {
        return reservationService.getReservationParAnneeUniversitaire(dateDebut,dateFin);
    }
    @PostMapping({"/reserverAvecPriorite/{numChambre}"})
    public ResponseEntity<String> reserverChambreAvecPriorite(@PathVariable("numChambre") Long numChambre, @RequestBody List<Long> cins, @RequestParam("anneeUniversitaire") String anneeUniversitaire) {
        LocalDate dateAnneeUniversitaire = null;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        } catch (Exception var6) {
            return ResponseEntity.badRequest().body("Date d'année universitaire invalide : " + anneeUniversitaire);
        }

        String message = this.reservationServicImpl.reserverChambreAvecPriorite(cins, numChambre, dateAnneeUniversitaire);
        if (!message.contains("Chambre introuvable") && !message.contains("Étudiant introuvable")) {
            return message.contains("Aucune chambre disponible") ? ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message) : ResponseEntity.ok(message);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }
}
