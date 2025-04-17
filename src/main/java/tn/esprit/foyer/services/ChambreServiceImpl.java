//package tn.esprit.foyer.services;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import tn.esprit.foyer.entities.*;
//import tn.esprit.foyer.repository.BlocRepository;
//import tn.esprit.foyer.repository.ChambreRepository;
//import tn.esprit.foyer.repository.FoyerRepository;
//import tn.esprit.foyer.repository.TacheRepository;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Slf4j
//@AllArgsConstructor
//public class ChambreServiceImpl implements IChambreService {
//
//    ChambreRepository chambreRepository;
//
//    BlocRepository blocRepository;
//
//    FoyerRepository foyerRepository;
//    TacheRepository tacheRepository;
//
//    @Override
//    public List<Chambre> retrieveAllChambres() {
//
//        System.out.println("in method retrieveAllChambres");
//        return chambreRepository.findAll();
//    }
//
//    @Override
//    public Chambre addChambre(Chambre c) {
//        return chambreRepository.save(c);
//    }
//
//    @Override
//    public Chambre updateChambre(Chambre c) {
//        return chambreRepository.save(c);
//    }
//
//    @Override
//    public Chambre retrieveChambre(Long idChambre) {
//
//
//        return chambreRepository.findById(idChambre).orElse(null);
//    }
//
//    @Override
//    public void removeChambre(Long idChambre) {
//        chambreRepository.deleteById(idChambre);
//    }
//
//    @Override
//    public List<Chambre> findByTypeCAndBlocIdBloc(TypeChambre typeChambre, Long idBloc) {
//        //   return chambreRepository.findByTypeCAndBlocIdBloc(typeChambre,idBloc);
//        return chambreRepository.findByTypeCAndBlocIdBloc(typeChambre, idBloc);
//    }
//
//    @Override
//    public List<Chambre> findByReservationsEstValid(Boolean estValid) {
//        // return chambreRepository.findByReservationsEstValid(estValid);
//        return chambreRepository.findByReservationsValide(estValid);
//    }
//
//    @Override
//    public List<Chambre> findByBlocIdBlocAndBlocCapaciteBlocGreaterThan(Long idBloc, Long capaciteBloc) {
//        //   return chambreRepository.findByBlocIdBlocAndBlocCapaciteBlocGreaterThan(idBloc,capaciteBloc);
//        return chambreRepository.findByBlocIdBlocAndBlocCapaciteBloc(idBloc, capaciteBloc);
//    }
//
//    @Override
//    public List<Chambre> getChambresParNomBloc(String nomBloc) {
//        return chambreRepository.findByBlocNomBloc(nomBloc);
//
//    }
//
//
//    @Override
//    public long nbChambreParTypeEtBloc(TypeChambre type, long idBloc) {
//
//        return chambreRepository.nbChambreParTypeEtBloc(type, idBloc);
//    }
//
//    @Override
//    public List<Chambre> getChambresNonReserveParNomFoyerEtTypeChambre(String nomFoyer, TypeChambre type) {
//        List<Chambre> chambresDisponibles = new ArrayList<>();
//        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
//        LocalDate endDate = LocalDate.of(LocalDate.now().getYear(), 12, 31);
//        Foyer f = foyerRepository.findByNomFoyer(nomFoyer);
//        Optional<List<Bloc>> blocsParFoyer = Optional.ofNullable(f.getBlocs());
//        if (blocsParFoyer.isPresent()) {
//            blocsParFoyer.get().forEach(bloc ->
//                    bloc.getChambres().forEach(chambre ->
//                    {
//                        if(chambre.getTypeC().equals(type)) {
//                            Long nbReservationChambre = chambreRepository.checkNbReservationsChambre(startDate, endDate, type, chambre.getNumeroChambre());
//                            if ((chambre.getTypeC().equals(TypeChambre.SIMPLE) && nbReservationChambre == 0) ||
//                                    (chambre.getTypeC().equals(TypeChambre.DOUBLE) && nbReservationChambre < 2) ||
//                                    (chambre.getTypeC().equals(TypeChambre.TRIPLE) && nbReservationChambre < 3)){
//                                chambresDisponibles.add(chambre);
//                            }
//                        }
//                    }));
//        }
//        return chambresDisponibles;
//    }
//
//  //  @Scheduled(fixedRate = 60000)
//    public void pourcentageChambreParTypeChambre() {
//        Integer nbTotalsChambres = chambreRepository.findAll().size();
//        log.info("nbTotalsChambres : " + nbTotalsChambres);
//        Arrays.stream(TypeChambre.values()).forEach(
//                typeChambre -> {
//                    Integer nbChambresParType = chambreRepository.nbChambresParType(typeChambre);
//                    Double pourcentageParType = (nbChambresParType.doubleValue() / nbTotalsChambres.doubleValue()) * 100;
//                    log.info("le pourcentage des chambres pour le type " + typeChambre + " est égale à "
//                            + pourcentageParType);
//                }
//        );
//    }
//
//
//    public String affecterMaintenance() {
//        List<Chambre> chambres = chambreRepository.findAll();
//
//        if (chambres.isEmpty()) {
//            log.info("Aucune chambre trouvée.");
//            return "Aucune chambre trouvée.";
//        }
//
//        int nbtache = 0;
//
//        for (Chambre chambre : chambres) {
//            for (Reservation reservation : chambre.getReservations()) {
//                LocalDate dateReservation = reservation.getAnneeUniversitaire();
//
//                if (dateReservation == null) {
//                    log.warn("Réservation sans date universitaire ignorée : {}", reservation.getIdReservation());
//                    continue; // Ignore cette réservation
//                }
//
//                TypeTache typeTache;
//                LocalDate today = LocalDate.now();
//
//                if (dateReservation.isAfter(today.minusDays(7))) {
//                    typeTache = TypeTache.MENAGERE;
//                } else if (dateReservation.isAfter(today.minusDays(30))) {
//                    typeTache = TypeTache.BRICOLAGE;
//                } else {
//                    typeTache = TypeTache.JARDINAGE;
//                }
//
//                if (reservation.getEtudiants() == null || reservation.getEtudiants().isEmpty()) {
//                    log.warn("Réservation {} sans étudiant assigné, tâche non créée.", reservation.getIdReservation());
//                    continue; // Ignore cette réservation
//                }
//
//                Etudiant etudiant = reservation.getEtudiants().iterator().next(); // Récupère un étudiant
//
//                Tache tache = new Tache();
//                tache.setEtudiant(etudiant);
//                tache.setTypeTache(typeTache);
//                tache.setDateTache(today);
//                tache.setDuree(2);
//                tache.setTarifHoraire(50.0f);
//
//                tacheRepository.save(tache);
//                nbtache++;
//                log.info("Tâche {} affectée à l'étudiant {} pour le type {}", nbtache, etudiant.getNomEt(), typeTache);
//            }
//        }
//
//        log.info("{} tâches affectées au total.", nbtache);
//        return nbtache + " tâches affectées.";
//    }
//
//
//
//}
