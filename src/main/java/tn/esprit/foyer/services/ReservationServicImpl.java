package tn.esprit.foyer.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.foyer.entities.Chambre;
import tn.esprit.foyer.entities.Etudiant;
import tn.esprit.foyer.entities.Reservation;
import tn.esprit.foyer.entities.TypeChambre;
import tn.esprit.foyer.repository.ChambreRepository;
import tn.esprit.foyer.repository.EtudiantRepository;
import tn.esprit.foyer.repository.ReservationRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
@Slf4j
public class ReservationServicImpl implements IReservationService {

    ReservationRepository reservationRepository;
    ChambreRepository chambreRepository;
    EtudiantRepository etudiantRepository;

    @Override
    public List<Reservation> retrieveAllReservations() {
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
    public Reservation retrieveReservation(String idReservation) {
        return reservationRepository.findById(idReservation).orElse(null);
    }

    @Override
    public void removeReservation(String idReservation){
            reservationRepository.deleteById(idReservation);

    }

    public String reserverChambreAvecPriorite(List<Long> cinList, Long numChambre, LocalDate anneeUniversitaire) {
        Chambre chambre = chambreRepository.findByNumeroChambre(numChambre);
        if (chambre == null) {
            throw new IllegalArgumentException("Chambre introuvable avec le numéro : " + numChambre);
        }

        long placesReserved = reservationRepository.countByChambreAndAnneeUniversitaire(chambre, anneeUniversitaire);
        long totalPlaces = switch (chambre.getTypeC()) {
            case SIMPLE -> 1L;
            case DOUBLE -> 2L;
            case TRIPLE -> 3L;
        };

        long availablePlaces = totalPlaces - placesReserved;
        List<Etudiant> etudiants = new ArrayList<>();

        for (Long cin : cinList) {
            Etudiant etudiant = etudiantRepository.findByCin(cin);
            if (etudiant == null) {
                throw new IllegalArgumentException("Étudiant introuvable avec le CIN : " + cin);
            }
            etudiants.add(etudiant);
        }

        etudiants.sort(Comparator.comparing(Etudiant::getDateNaissance));
        StringBuilder resultMessage = new StringBuilder();
        List<Etudiant> etudiantsAffectes = new ArrayList<>();
        List<Etudiant> etudiantsRediriges = new ArrayList<>();
        Chambre autreChambre = null;

        for (Etudiant etudiant : etudiants) {
            if (availablePlaces > 0) {
                Reservation reservation = new Reservation();
                reservation.setChambre(chambre);
                reservation.setAnneeUniversitaire(anneeUniversitaire);
                reservation.setEstValid(true);
                reservation.setEtudiants(List.of(etudiant));
                reservationRepository.save(reservation);
                etudiantsAffectes.add(etudiant);
                availablePlaces--;
            } else {
                if (autreChambre == null) {
                    autreChambre = chambreRepository.findFirstByTypeCAndNumeroChambreNot(chambre.getTypeC(), numChambre);
                }
                if (autreChambre != null) {
                    Reservation reservation = new Reservation();
                    reservation.setChambre(autreChambre);
                    reservation.setAnneeUniversitaire(anneeUniversitaire);
                    reservation.setEstValid(true);
                    reservation.setEtudiants(List.of(etudiant));
                    reservationRepository.save(reservation);
                    etudiantsRediriges.add(etudiant);
                }
            }
        }

        resultMessage.append("Étudiants affectés à la chambre ").append(numChambre).append(": ");
        etudiantsAffectes.forEach(et -> resultMessage.append(et.getCin()).append(" "));

        resultMessage.append("\nÉtudiants redirigés vers une autre chambre (")
                .append(autreChambre != null ? autreChambre.getNumeroChambre() : "aucune chambre trouvée")
                .append("): ");
        etudiantsRediriges.forEach(et -> resultMessage.append(et.getCin()).append(" "));

        return resultMessage.toString();
    }

    @Transactional
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(Reservation res, Long numChambre, long cin) {

        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 12, 31);
        Etudiant e = etudiantRepository.findByCin(cin);
        Chambre c = chambreRepository.findByNumeroChambre(numChambre);
        // id selon la convention demandé
        res.setIdReservation(numChambre + e.getCin().toString() + LocalDate.now().getYear());
        res.setEstValid(true);
        // affecter étudiants aux réservations
        List<Etudiant> etudiants = new ArrayList<>();
        if (res.getEtudiants() != null) {
            etudiants.addAll(res.getEtudiants());
        }
        etudiants.add(e);
        res.setEtudiants(etudiants);
        if (c.getReservations() != null) {
            Integer reservationSize = reservationRepository.getReservationsCurrentYear(startDate,endDate,numChambre);
            switch (reservationSize) {
                case 0:
                    log.info("case reservation vide");
                    Reservation r = reservationRepository.save(res);
                    c.getReservations().add(r);
                    chambreRepository.save(c);
                    break;
                case 1:
                    log.info("case reservation courante egale à 1");
                    if (c.getTypeC().equals(TypeChambre.DOUBLE) || c.getTypeC().equals(TypeChambre.TRIPLE)) {
                        Reservation r1 = reservationRepository.save(res); // on peut affecter des reservations a la chambre sans la sauvegarder
                        c.getReservations().add(r1);
                        chambreRepository.save(c);

                    }
                    else {
                        log.info("chambre simple déja réservée");
                    }
                    break;
                case 2:
                    log.info("case reservation courante egale à 2");
                    if (c.getTypeC().equals(TypeChambre.TRIPLE)) {
                        Reservation r2 = reservationRepository.save(res); // on peut affecter des reservations a la cambre sans la sauvegarder
                        c.getReservations().add(r2);
                        chambreRepository.save(c);

                    }
                    else {
                        log.info("chambre double déja complete");
                    }
                    break;
                default:
                    log.info("case default");
                    log.info("Capacité chambre atteinte");
            }
        } else {
            Reservation r = reservationRepository.save(res);
            List<Reservation> reservations = new ArrayList<>();
            reservations.add(r);
            c.setReservations(reservations);
            chambreRepository.save(c);
        }
        return null;
    }

    @Override
    public List<Reservation> getReservationParAnneeUniversitaire(LocalDate dateDebut, LocalDate dateFin) {
        return reservationRepository.findByAnneeUniversitaireBetween(dateDebut,dateFin);
    }

  //  @Scheduled(fixedRate = 60000)
    public void nbPlacesDisponibleParChambreAnneeEnCours() {
        LocalDate currentdate = LocalDate.now();
        LocalDate dateDebut = LocalDate.of(currentdate.getYear(), 12, 31);
        LocalDate dateFin = LocalDate.of(currentdate.getYear(), 1, 1);
        List<Chambre> chambresDisponibles = chambreRepository.findAll();
        chambresDisponibles.forEach(
                chambre -> {
                    //       AtomicReference<Integer> nbChambresOccupes = new AtomicReference<>(0);
                    AtomicReference<Integer> nbChambresOccupes = new AtomicReference<>(0);

                    if (chambre.getReservations() != null) {
                        List<Reservation> reservations = chambre.getReservations();
                        reservations.stream().forEach(
                                reservation -> {
                                    if (reservation.getEstValid() && reservation.getAnneeUniversitaire().isAfter(dateDebut) && reservation.getAnneeUniversitaire().isBefore(dateFin))
                                        nbChambresOccupes.getAndSet(nbChambresOccupes.get() + 1);
                                }
                        );
                    }
                    if (chambre.getTypeC().equals(TypeChambre.SIMPLE)) {
                        log.info("nb places restantes en " + currentdate.getYear() + " pour la chambre " + chambre.getNumeroChambre()
                                + " est égale à " + (1 - nbChambresOccupes.get()));
                    } else if (chambre.getTypeC().equals(TypeChambre.DOUBLE)) {
                        log.info("nb places restantes en " + currentdate.getYear() + " pour la chambre " + chambre.getNumeroChambre()
                                + " est égale à " + (2 - nbChambresOccupes.get()));
                    } else { // cas triple
                        log.info("nb places restantes en " + currentdate.getYear() + " pour la chambre " + chambre.getNumeroChambre()
                                + " est égale à " + (3 - nbChambresOccupes.get()));
                    }


                }
        );

    }
}
