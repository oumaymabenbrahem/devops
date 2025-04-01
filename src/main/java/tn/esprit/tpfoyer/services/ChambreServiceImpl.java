package tn.esprit.tpfoyer.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entities.Bloc;
import tn.esprit.tpfoyer.entities.Chambre;
import tn.esprit.tpfoyer.entities.TypeChambre;
import tn.esprit.tpfoyer.repositories.ChambreRepository;
import tn.esprit.tpfoyer.repositories.ReservationRepository;
import tn.esprit.tpfoyer.entities.Reservation;
import tn.esprit.tpfoyer.entities.Etudiant;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ChambreServiceImpl implements  IChambreService {
    ChambreRepository chambreRepository;

     ReservationRepository reservationRepository;

    @Override
    public List<Chambre> retrieAllChambres() {
        return chambreRepository.findAll();
    }

    @Override
    public Chambre addChambre(Chambre c) {
        return chambreRepository.save(c);
    }

    @Override
    public Chambre updateChambre(Chambre c) {
        return chambreRepository.save(c);
    }

    @Override
    public Chambre retrieChambre(Long idChambre) {
        return chambreRepository.findById(idChambre).get();
    }

    @Override
    public void removeChambre(Long idChambre) {
       chambreRepository.deleteById(idChambre);

    }

    @Override
    public List<Chambre> findByTypeChambreAndBlocNomBloc(TypeChambre typeChambre, String nomBloc) {
        return chambreRepository.findByTypeChambreAndBlocNomBloc(typeChambre, nomBloc);
    }

    @Override
    public List<Chambre> findByReservationsEstValide(boolean estValide) {
        return chambreRepository.findByReservationsEstValide(estValide);
    }

    @Override
    public List<Chambre> findByBlocNomBlocAndBlocCapaciteBlocGreaterThan(String nomBloc, long capaciteBloc) {
        return chambreRepository.findByBlocNomBlocAndBlocCapaciteBlocGreaterThan(nomBloc, capaciteBloc);
    }
    public void reserverChambre(Chambre chambre, Etudiant etudiant) {
        // Déterminer la capacité de la chambre en fonction de son type
        int capaciteMax = switch (chambre.getTypeChambre()) {
            case SIMPLE -> 1;
            case DOUBLE -> 2;
            case TRIPLE -> 3;
        };

        // Vérifier le nombre de réservations actuelles pour cette chambre
        long nbReservations = reservationRepository.countByChambre(chambre);

        if (nbReservations >= capaciteMax) {
            throw new RuntimeException("Cette chambre est déjà pleine.");
        }

        // Créer une nouvelle réservation
        Reservation reservation = new Reservation(chambre, etudiant);
        reservationRepository.save(reservation);
    }

}
