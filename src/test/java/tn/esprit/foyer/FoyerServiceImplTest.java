package tn.esprit.foyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName; // Importer DisplayName
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.foyer.configuration.EntityNotFoundExceptionById;
import tn.esprit.foyer.entities.*;
import tn.esprit.foyer.repository.FoyerRepository;
import tn.esprit.foyer.repository.ReservationRepository;
import tn.esprit.foyer.services.FoyerServiceImpl;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests Unitaires pour FoyerServiceImpl - Statistiques d'Occupation") // Nom pour la classe entière
class FoyerServiceImplTest {

    @InjectMocks
    private FoyerServiceImpl foyerService;

    @Mock
    private FoyerRepository foyerRepository;

    @Mock
    private ReservationRepository reservationRepository;

    private Foyer mockFoyer;
    private Bloc mockBloc1;
    private Bloc mockBloc2;
    private Chambre mockChambreS1;
    private Chambre mockChambreD1;
    private Chambre mockChambreD2;
    private Chambre mockChambreT1;

    private static final Long FOYER_ID = 1L;
    private static final Long NON_EXISTENT_FOYER_ID = 99L;
    private static final String NOM_FOYER = "Foyer Central";
    private static final Long CAPACITE_DECLAREE = 150L;

    @BeforeEach
    void setUp() {
        // (Setup identique à avant)
        mockChambreS1 = Chambre.builder().idChambre(101L).numeroChambre(1L).typeC(TypeChambre.SIMPLE).build();
        mockChambreD1 = Chambre.builder().idChambre(102L).numeroChambre(2L).typeC(TypeChambre.DOUBLE).build();
        mockChambreD2 = Chambre.builder().idChambre(103L).numeroChambre(3L).typeC(TypeChambre.DOUBLE).build();
        mockChambreT1 = Chambre.builder().idChambre(104L).numeroChambre(4L).typeC(TypeChambre.TRIPLE).build();
        mockBloc1 = Bloc.builder().idBloc(201L).nomBloc("Bloc A").chambres(Arrays.asList(mockChambreS1, mockChambreD1)).build();
        mockBloc2 = Bloc.builder().idBloc(202L).nomBloc("Bloc B").chambres(Arrays.asList(mockChambreD2, mockChambreT1)).build();
        mockFoyer = Foyer.builder()
                .idFoyer(FOYER_ID)
                .nomFoyer(NOM_FOYER)
                .capaciteFoyer(CAPACITE_DECLAREE)
                .blocs(Arrays.asList(mockBloc1, mockBloc2))
                .build();
        mockBloc1.setFoyer(mockFoyer);
        mockBloc2.setFoyer(mockFoyer);
    }

    @Test
    @DisplayName("Cas nominal : Calcul des statistiques d'occupation réussi")
    void testGetFoyerOccupancyStats_Success() {
        // Arrange
        LocalDate startDate = LocalDate.of(2024, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(2024, Month.DECEMBER, 31);
        long expectedRealCapacity = 8L; // 1 + 2 + 2 + 3
        long expectedOccupiedCount = 5L;
        List<Long> expectedChambreIds = Arrays.asList(101L, 102L, 103L, 104L);

        when(foyerRepository.findById(FOYER_ID)).thenReturn(Optional.of(mockFoyer));
        when(reservationRepository.countValidReservationsForChambresInPeriod(eq(expectedChambreIds), eq(startDate), eq(endDate)))
                .thenReturn(expectedOccupiedCount);

        // Act
        FoyerOccupancyStats stats = foyerService.getFoyerOccupancyStats(FOYER_ID, startDate, endDate);

        // ----- AFFICHAGE DU RAPPORT (DTO) -----
        System.out.println("\n--- CONTENU DU RAPPORT (DTO) ---");
        System.out.println(stats); // Appel implicite à stats.toString()
        System.out.println("--------------------------------\n");
        // --------------------------------------

        // Assert
        assertNotNull(stats, "Le DTO de statistiques ne devrait pas être null");

        // Regrouper les assertions pour une meilleure lisibilité en cas d'échec multiple
        assertAll("Vérification des statistiques calculées",
                () -> assertEquals(FOYER_ID, stats.getFoyerId(), "L'ID du foyer dans le DTO est incorrect"),
                () -> assertEquals(NOM_FOYER, stats.getNomFoyer(), "Le nom du foyer dans le DTO est incorrect"),
                () -> assertEquals(startDate, stats.getPeriodeAnalyseDebut(), "La date de début de période est incorrecte"),
                () -> assertEquals(endDate, stats.getPeriodeAnalyseFin(), "La date de fin de période est incorrecte"),
                () -> assertEquals(CAPACITE_DECLAREE, stats.getCapaciteDeclaree(), "La capacité déclarée est incorrecte"),
                () -> assertEquals(expectedRealCapacity, stats.getCapaciteReelleTotale(), "La capacité réelle totale calculée est incorrecte"),
                () -> assertEquals(expectedOccupiedCount, stats.getPlacesOccupees(), "Le nombre de places occupées calculé est incorrect"),
                () -> assertEquals(expectedRealCapacity - expectedOccupiedCount, stats.getPlacesDisponibles(), "Le nombre de places disponibles calculé est incorrect"),
                () -> assertEquals((double) expectedOccupiedCount / expectedRealCapacity * 100.0, stats.getTauxOccupation(), 0.01, "Le taux d'occupation calculé est incorrect")
        );

        // Verify
        verify(foyerRepository).findById(FOYER_ID);
        verify(reservationRepository).countValidReservationsForChambresInPeriod(eq(expectedChambreIds), eq(startDate), eq(endDate));
    }

    @Test
    @DisplayName("Cas d'erreur : Foyer non trouvé par ID")
    void testGetFoyerOccupancyStats_FoyerNotFound() {
        // Arrange
        when(foyerRepository.findById(NON_EXISTENT_FOYER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundExceptionById exception = assertThrows(EntityNotFoundExceptionById.class, () -> {
            foyerService.getFoyerOccupancyStats(NON_EXISTENT_FOYER_ID, null, null);
        }, "Une exception EntityNotFoundExceptionById aurait dû être levée");

        assertTrue(exception.getMessage().contains("Foyer non trouvé avec l'ID : " + NON_EXISTENT_FOYER_ID),
                "Le message d'exception ne contient pas l'ID attendu");

        // Verify
        verify(foyerRepository).findById(NON_EXISTENT_FOYER_ID);
        verifyNoInteractions(reservationRepository);
    }

    @Test
    @DisplayName("Cas limite : Foyer sans blocs ou chambres")
    void testGetFoyerOccupancyStats_NoBlocsOrChambres() {
        // Arrange
        Foyer foyerSansBlocs = Foyer.builder()
                .idFoyer(FOYER_ID).nomFoyer(NOM_FOYER).capaciteFoyer(CAPACITE_DECLAREE)
                .blocs(Collections.emptyList()) // Ou null
                .build();
        when(foyerRepository.findById(FOYER_ID)).thenReturn(Optional.of(foyerSansBlocs));

        // Act
        FoyerOccupancyStats stats = foyerService.getFoyerOccupancyStats(FOYER_ID, null, null);

        // ----- AFFICHAGE DU RAPPORT (DTO) -----
        System.out.println("\n--- CONTENU DU RAPPORT (DTO - Foyer Vide) ---");
        System.out.println(stats); // Appel implicite à stats.toString()
        System.out.println("-------------------------------------------\n");
        // --------------------------------------


        // Assert
        assertNotNull(stats, "Le DTO ne devrait pas être null même sans blocs/chambres");
        assertAll("Vérification des statistiques pour un foyer vide",
                () -> assertEquals(FOYER_ID, stats.getFoyerId(), "L'ID du foyer est incorrect"),
                () -> assertEquals(0, stats.getCapaciteReelleTotale(), "La capacité réelle devrait être 0"),
                () -> assertEquals(0, stats.getPlacesOccupees(), "Les places occupées devraient être 0"),
                () -> assertEquals(0, stats.getPlacesDisponibles(), "Les places disponibles devraient être 0"),
                () -> assertEquals(0.0, stats.getTauxOccupation(), 0.01, "Le taux d'occupation devrait être 0.0")
        );

        // Verify
        verify(foyerRepository).findById(FOYER_ID);
        verifyNoInteractions(reservationRepository); // Pas d'appel si capacité réelle = 0
    }

    @Test
    @DisplayName("Cas limite : Foyer avec chambres mais sans réservations")
    void testGetFoyerOccupancyStats_NoReservations() {
        // Arrange
        LocalDate startDate = LocalDate.of(2024, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(2024, Month.DECEMBER, 31);
        long expectedRealCapacity = 8L;
        long expectedOccupiedCount = 0L; // Aucune réservation
        List<Long> expectedChambreIds = Arrays.asList(101L, 102L, 103L, 104L);

        when(foyerRepository.findById(FOYER_ID)).thenReturn(Optional.of(mockFoyer));
        when(reservationRepository.countValidReservationsForChambresInPeriod(eq(expectedChambreIds), eq(startDate), eq(endDate)))
                .thenReturn(expectedOccupiedCount); // Retourne 0

        // Act
        FoyerOccupancyStats stats = foyerService.getFoyerOccupancyStats(FOYER_ID, startDate, endDate);

        // ----- AFFICHAGE DU RAPPORT (DTO) -----
        System.out.println("\n--- CONTENU DU RAPPORT (DTO - Sans Réservations) ---");
        System.out.println(stats); // Appel implicite à stats.toString()
        System.out.println("--------------------------------------------------\n");
        // --------------------------------------

        // Assert
        assertNotNull(stats, "Le DTO ne devrait pas être null");
        assertAll("Vérification des statistiques sans réservations",
                () -> assertEquals(FOYER_ID, stats.getFoyerId()),
                () -> assertEquals(expectedRealCapacity, stats.getCapaciteReelleTotale(), "La capacité réelle est incorrecte"),
                () -> assertEquals(expectedOccupiedCount, stats.getPlacesOccupees(), "Les places occupées devraient être 0"),
                () -> assertEquals(expectedRealCapacity, stats.getPlacesDisponibles(), "Toutes les places devraient être disponibles"),
                () -> assertEquals(0.0, stats.getTauxOccupation(), 0.01, "Le taux d'occupation devrait être 0.0%")
        );

        // Verify
        verify(foyerRepository).findById(FOYER_ID);
        verify(reservationRepository).countValidReservationsForChambresInPeriod(eq(expectedChambreIds), eq(startDate), eq(endDate));
    }

    @Test
    @DisplayName("Cas nominal : Calcul avec dates par défaut (année universitaire courante)")
    void testGetFoyerOccupancyStats_DefaultDates() {
        // Arrange
        long expectedOccupiedCount = 3L;
        List<Long> expectedChambreIds = Arrays.asList(101L, 102L, 103L, 104L);

        LocalDate now = LocalDate.now();
        LocalDate expectedDefaultDebut;
        LocalDate expectedDefaultFin;
        // Logique identique à celle du service pour déterminer les dates par défaut
        if (now.getMonthValue() >= Month.SEPTEMBER.getValue()) {
            expectedDefaultDebut = LocalDate.of(now.getYear(), Month.SEPTEMBER, 1);
            expectedDefaultFin = LocalDate.of(now.getYear() + 1, Month.AUGUST, 31);
        } else {
            expectedDefaultDebut = LocalDate.of(now.getYear() - 1, Month.SEPTEMBER, 1);
            expectedDefaultFin = LocalDate.of(now.getYear(), Month.AUGUST, 31);
        }

        when(foyerRepository.findById(FOYER_ID)).thenReturn(Optional.of(mockFoyer));
        when(reservationRepository.countValidReservationsForChambresInPeriod(eq(expectedChambreIds), eq(expectedDefaultDebut), eq(expectedDefaultFin)))
                .thenReturn(expectedOccupiedCount);

        // Act
        FoyerOccupancyStats stats = foyerService.getFoyerOccupancyStats(FOYER_ID, null, null); // Appel sans dates

        // ----- AFFICHAGE DU RAPPORT (DTO) -----
        System.out.println("\n--- CONTENU DU RAPPORT (DTO - Dates Défaut) ---");
        System.out.println(stats); // Appel implicite à stats.toString()
        System.out.println("---------------------------------------------\n");
        // --------------------------------------

        // Assert
        assertNotNull(stats, "Le DTO ne devrait pas être null");
        assertAll("Vérification des statistiques avec dates par défaut",
                () -> assertEquals(FOYER_ID, stats.getFoyerId()),
                () -> assertEquals(expectedDefaultDebut, stats.getPeriodeAnalyseDebut(), "La date de début par défaut est incorrecte"),
                () -> assertEquals(expectedDefaultFin, stats.getPeriodeAnalyseFin(), "La date de fin par défaut est incorrecte"),
                () -> assertEquals(8L, stats.getCapaciteReelleTotale(), "La capacité réelle est incorrecte"),
                () -> assertEquals(expectedOccupiedCount, stats.getPlacesOccupees(), "Le nombre de places occupées est incorrect"),
                () -> assertEquals(8L - expectedOccupiedCount, stats.getPlacesDisponibles(), "Le nombre de places disponibles est incorrect"),
                () -> assertEquals((double) expectedOccupiedCount / 8.0 * 100.0, stats.getTauxOccupation(), 0.01, "Le taux d'occupation est incorrect")
        );

        // Verify
        verify(foyerRepository).findById(FOYER_ID);
        verify(reservationRepository).countValidReservationsForChambresInPeriod(eq(expectedChambreIds), eq(expectedDefaultDebut), eq(expectedDefaultFin));
    }
}