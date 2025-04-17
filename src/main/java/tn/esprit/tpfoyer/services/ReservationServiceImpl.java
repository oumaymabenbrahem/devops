package tn.esprit.tpfoyer.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entities.Etudiant;
import tn.esprit.tpfoyer.entities.Reservation;
import tn.esprit.tpfoyer.repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor


public class ReservationServiceImpl implements  IReservationService{
    ReservationRepository reservationRepository;
    @Override
    public List<Reservation> retrieAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation addReservation(Reservation r) {
        return reservationRepository.save(r);
    }

    @Override
    public Reservation updateReservation(Reservation r) {
        return reservationRepository.save(r);
    }

    @Override
    public Reservation retrieReservation(Long idReservation) {
        return reservationRepository.findById(idReservation).get();
    }

    @Override
    public void removeReservation(Long idReservation) {
       reservationRepository.deleteById(idReservation);

    }
    public boolean validateReservation(Long reservationId) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if (optionalReservation.isEmpty()) return false; // Reservation not found

        Reservation reservation = optionalReservation.get();

        // Check if reservation has NO students
        if (reservation.getEtudiants() == null || reservation.getEtudiants().isEmpty()) {
            return false; //  Validation fails if there are no students
        }

        //  Validate that all students have a valid CIN
        for (Etudiant etudiant : reservation.getEtudiants()) {
            if (etudiant.getCin() == null || etudiant.getCin() <= 0) {
                return false; // Invalid student CIN
            }
        }

        //  If all checks pass, mark the reservation as valid
        reservation.setEstValide(true);
        reservationRepository.save(reservation);

        return true; //  Only return true if conditions are met
    }

    public List<Reservation> expireOldReservations() {
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);

        List<Reservation> oldReservations = reservationRepository.findByAnneeUniversitaireBefore(java.sql.Date.valueOf(oneYearAgo));

        for (Reservation reservation : oldReservations) {
            reservation.setEstValide(false);
        }

        return reservationRepository.saveAll(oldReservations);
    }
}
