package tn.esprit.tpfoyer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entities.Reservation;
import tn.esprit.tpfoyer.repositories.ReservationRepository;
import tn.esprit.tpfoyer.services.ReservationServiceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    public void testExpireOldReservations() {
        // Given: Reservations older than 1 year
        Reservation oldReservation1 = new Reservation(1L, Date.valueOf(LocalDate.now().minusYears(2)), true);
        Reservation oldReservation2 = new Reservation(2L, Date.valueOf(LocalDate.now().minusYears(3)), true);
        List<Reservation> expiredReservations = Arrays.asList(oldReservation1, oldReservation2);

        when(reservationRepository.findByAnneeUniversitaireBefore(any())).thenReturn(expiredReservations);
        when(reservationRepository.saveAll(anyList())).thenReturn(expiredReservations);

        // When: Calling the expiration method
        List<Reservation> result = reservationService.expireOldReservations();

        // Then: Verify reservations are marked as expired
        assertEquals(2, result.size());
        assertFalse(result.get(0).isEstValide()); // Check that status changed
        assertFalse(result.get(1).isEstValide());

        // Verify interactions
        verify(reservationRepository, times(1)).findByAnneeUniversitaireBefore(any());
        verify(reservationRepository, times(1)).saveAll(anyList());
    }
}
