package tn.esprit.tpfoyer.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entities.Reservation;
import tn.esprit.tpfoyer.repositories.ChambreRepository;
import tn.esprit.tpfoyer.repositories.ReservationRepository;
import tn.esprit.tpfoyer.entities.Chambre;
import tn.esprit.tpfoyer.entities.Etudiant;
import tn.esprit.tpfoyer.entities.TypeChambre;

import java.util.List;
@Service
@Slf4j
@AllArgsConstructor


public class ReservationServiceImpl implements  IReservationService{
    ReservationRepository reservationRepository;
    ChambreRepository chambreRepository;
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

    public Chambre attribuerChambreAutomatiquement(Etudiant etudiant, TypeChambre typeChambre) {
        // Récupérer les chambres disponibles de ce type
        List<Chambre> chambresDisponibles = chambreRepository.findChambresDisponiblesParType(typeChambre);

        if (chambresDisponibles.isEmpty()) {
            throw new RuntimeException("Aucune chambre disponible pour ce type.");
        }

        // Trier les chambres par numéro croissant et prendre la première
        chambresDisponibles.sort((c1, c2) -> Long.compare(c1.getNumChambre(), c2.getNumChambre()));
        Chambre chambreAttribuee = chambresDisponibles.get(0);

        // Créer et sauvegarder la réservation
        Reservation reservation = new Reservation(chambreAttribuee, etudiant);
        reservationRepository.save(reservation);

        return chambreAttribuee;
    }

    public Reservation getReservation(Long id) {
        // Logique pour obtenir une réservation par son ID
        return reservationRepository.findById(id).orElse(null);
    }

}
