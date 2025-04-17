package tn.esprit.tpfoyer.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entities.Chambre;
import tn.esprit.tpfoyer.entities.Etudiant;
import tn.esprit.tpfoyer.entities.Reservation;
import tn.esprit.tpfoyer.entities.TypeChambre;
import tn.esprit.tpfoyer.repositories.ChambreRepository;
import tn.esprit.tpfoyer.repositories.ReservationRepository;
import tn.esprit.tpfoyer.services.ReservationServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ReservationServiceImplTest {
    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
    }

    @Test
    void testAttribuerChambreAutomatiquement_Succes() {
        // Simuler des chambres disponibles du type DOUBLE
        Chambre chambre1 = new Chambre(1L, 101, TypeChambre.DOUBLE);
        Chambre chambre2 = new Chambre(2L, 102, TypeChambre.DOUBLE);

        List<Chambre> chambresDisponibles = Arrays.asList(chambre1, chambre2);

        // Simuler le comportement du repository
        when(chambreRepository.findChambresDisponiblesParType(TypeChambre.DOUBLE)).thenReturn(chambresDisponibles);

        // Exécuter la réservation automatique
        System.out.println("Test: Attribuer chambre automatiquement - Succès");
        Chambre chambreAttribuee = reservationService.attribuerChambreAutomatiquement(etudiant, TypeChambre.DOUBLE);

        // Vérifier que la chambre avec le plus petit numéro a été attribuée
        System.out.println("Chambre attribuée : " + chambreAttribuee.getNumChambre());
        assertEquals(chambre1, chambreAttribuee);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        System.out.println("Réservation effectuée pour l'étudiant : " + etudiant.getIdEtudiant());
    }

    @Test
    void testAttribuerChambreAutomatiquement_Echec_PasDeChambreDisponible() {
        // Simuler aucune chambre disponible du type demandé
        when(chambreRepository.findChambresDisponiblesParType(TypeChambre.SIMPLE)).thenReturn(List.of());

        System.out.println("Test: Attribuer chambre automatiquement - Echec (Aucune chambre disponible)");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            reservationService.attribuerChambreAutomatiquement(etudiant, TypeChambre.SIMPLE);
        });

        System.out.println("Exception attendue : " + exception.getMessage());
        assertEquals("Aucune chambre disponible pour ce type.", exception.getMessage());

        // Vérifier qu'aucune réservation n'a été enregistrée
        verify(reservationRepository, never()).save(any(Reservation.class));
        System.out.println("Aucune réservation effectuée.");
    }
}
