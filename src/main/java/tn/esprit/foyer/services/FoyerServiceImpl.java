package tn.esprit.foyer.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.foyer.configuration.EntityNotFoundExceptionById;
import tn.esprit.foyer.entities.Bloc;
import tn.esprit.foyer.entities.Chambre;
import tn.esprit.foyer.entities.Foyer;
import tn.esprit.foyer.entities.FoyerOccupancyStats;
import tn.esprit.foyer.repository.BlocRepository;
import tn.esprit.foyer.repository.ChambreRepository;
import tn.esprit.foyer.repository.FoyerRepository;
import tn.esprit.foyer.repository.ReservationRepository;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FoyerServiceImpl implements  IFoyerService{

    FoyerRepository foyerRepository;
    BlocRepository blocRepository;
    ChambreRepository chambreRepository; // Ajoutez si ce n'est pas déjà injecté
    ReservationRepository reservationRepository; // Ajoutez si ce n'est pas déjà injecté
    @Override
    public List<Foyer> retrieveAllFoyers() {
        return foyerRepository.findAll();
    }

    @Override
    public Foyer addFoyer(Foyer f) {

        return foyerRepository.save(f);
    }

    @Override
    public Foyer addFoyerWithBloc(Foyer f) {

        Foyer foyer = foyerRepository.save(f);
        f.getBlocs().forEach(bloc ->
        {
            bloc.setFoyer(foyer);
            blocRepository.save(bloc);
        });
        return foyer;
    }

    @Override
    public Foyer updateFoyer(Foyer f) {
        return foyerRepository.save(f);
    }

    @Override
    public Foyer retrieveFoyer(Long idFoyer) {
            return foyerRepository.findById(idFoyer).orElse(null);
    }

    @Override
    public void removeFoyer(Long idFoyer) {

        foyerRepository.deleteById(idFoyer);
    }
    @Override
//    @Transactional(readOnly = true) // Bon pour les méthodes de lecture seule pour la performance et la cohérence
    public FoyerOccupancyStats getFoyerOccupancyStats(Long foyerId, LocalDate dateDebut, LocalDate dateFin) {
        log.info("Début du calcul des statistiques d'occupation pour le foyer ID: {}", foyerId);

        // 1. Récupérer le Foyer
        Foyer foyer = foyerRepository.findById(foyerId)
                .orElseThrow(() -> new EntityNotFoundExceptionById("Foyer non trouvé avec l'ID : " + foyerId));

        // 2. Déterminer la période d'analyse
        // Si dates non fournies, utiliser l'année universitaire courante (ex: 1 Sept - 31 Aout)
        LocalDate now = LocalDate.now();
        LocalDate defaultDebut;
        LocalDate defaultFin;
        if (now.getMonthValue() >= Month.SEPTEMBER.getValue()) {
            defaultDebut = LocalDate.of(now.getYear(), Month.SEPTEMBER, 1);
            defaultFin = LocalDate.of(now.getYear() + 1, Month.AUGUST, 31);
        } else {
            defaultDebut = LocalDate.of(now.getYear() - 1, Month.SEPTEMBER, 1);
            defaultFin = LocalDate.of(now.getYear(), Month.AUGUST, 31);
        }

        LocalDate debutAnalyse = (dateDebut != null) ? dateDebut : defaultDebut;
        LocalDate finAnalyse = (dateFin != null) ? dateFin : defaultFin;

        log.debug("Période d'analyse: {} au {}", debutAnalyse, finAnalyse);

        // 3. Calculer la capacité réelle totale
        long capaciteReelleTotale = 0;
        if (foyer.getBlocs() != null) {
            for (Bloc bloc : foyer.getBlocs()) {
                if (bloc.getChambres() != null) {
                    for (Chambre chambre : bloc.getChambres()) {
                        switch (chambre.getTypeC()) {
                            case SIMPLE:
                                capaciteReelleTotale += 1;
                                break;
                            case DOUBLE:
                                capaciteReelleTotale += 2;
                                break;
                            case TRIPLE:
                                capaciteReelleTotale += 3;
                                break;
                        }
                    }
                }
            }
        }
        log.debug("Capacité réelle totale calculée: {}", capaciteReelleTotale);


        // 4. Calculer les places occupées (Nécessite une requête optimisée - voir Étape 4)
        long placesOccupees = 0;
        if (capaciteReelleTotale > 0 && foyer.getBlocs() != null) {
            // Récupérer les IDs de toutes les chambres de ce foyer
            List<Long> chambreIds = foyer.getBlocs().stream()
                    .flatMap(bloc -> bloc.getChambres() != null ? bloc.getChambres().stream() : null)
                    .filter(chambre -> chambre != null)
                    .map(Chambre::getIdChambre)
                    .collect(Collectors.toList());

            if (!chambreIds.isEmpty()) {
                // Utiliser la méthode du repository (à créer à l'étape 4)
                placesOccupees = reservationRepository.countValidReservationsForChambresInPeriod(chambreIds, debutAnalyse, finAnalyse);

            }
        }
        log.debug("Places occupées calculées: {}", placesOccupees);

        // 5. Calculer les places disponibles et le taux d'occupation
        long placesDisponibles = capaciteReelleTotale - placesOccupees;
        if (placesDisponibles < 0) {
            log.warn("Incohérence détectée: Places disponibles négatives ({}) pour le foyer {}", placesDisponibles, foyerId);
            placesDisponibles = 0; // Ou gérer autrement
        }

        double tauxOccupation = 0.0;
        if (capaciteReelleTotale > 0) {
            tauxOccupation = ((double) placesOccupees / capaciteReelleTotale) * 100.0;
        }
        log.debug("Taux d'occupation calculé: {}%", tauxOccupation);

        // 6. Construire et retourner le DTO
        return FoyerOccupancyStats.builder()
                .foyerId(foyer.getIdFoyer())
                .nomFoyer(foyer.getNomFoyer())
                .periodeAnalyseDebut(debutAnalyse)
                .periodeAnalyseFin(finAnalyse)
                .capaciteDeclaree(foyer.getCapaciteFoyer())
                .capaciteReelleTotale(capaciteReelleTotale)
                .placesOccupees(placesOccupees)
                .placesDisponibles(placesDisponibles)
                .tauxOccupation(tauxOccupation)
                .build();
    }
}
