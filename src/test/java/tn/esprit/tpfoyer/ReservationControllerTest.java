package tn.esprit.tpfoyer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer.Controllers.ReservationRestController;
import tn.esprit.tpfoyer.entities.Reservation;
import tn.esprit.tpfoyer.services.ReservationServiceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ReservationRestController.class)
public class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationServiceImpl reservationService;

    @Test
    public void testExpireOldReservationsAPI() throws Exception {
        // Given: Two expired reservations
        Reservation reservation1 = new Reservation(1L, Date.valueOf(LocalDate.now().minusYears(2)), false);
        Reservation reservation2 = new Reservation(2L, Date.valueOf(LocalDate.now().minusYears(3)), false);
        List<Reservation> expiredReservations = Arrays.asList(reservation1, reservation2);

        when(reservationService.expireOldReservations()).thenReturn(expiredReservations);

        // When: Calling the API //mockmvc t5alikta3mel request
        mockMvc.perform(put("/api/reservations/expire-old"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2))) // Ensure 2 reservations are returned
                .andExpect(jsonPath("$[0].idReservation", is(1))) // Check first reservation ID
                .andExpect(jsonPath("$[1].idReservation", is(2))); // Check second reservation ID

        // Verify interactions
        verify(reservationService, times(1)).expireOldReservations();
    }

}
