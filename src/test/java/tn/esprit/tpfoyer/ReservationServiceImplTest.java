package tn.esprit.tpfoyer;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpfoyer.entities.Etudiant;
import tn.esprit.tpfoyer.entities.Reservation;
import tn.esprit.tpfoyer.repositories.EtudiantRepository;
import tn.esprit.tpfoyer.repositories.ReservationRepository;
import tn.esprit.tpfoyer.services.ReservationServiceImpl;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@Rollback
public class ReservationServiceImplTest {
//    Find the reservation by its ID from the ReservationRepository.
//            Check if the reservation exists (Optional.isEmpty() case).
//    Validate that all students in the reservation have a valid CIN (i.e., student identity number should not be null or negative).
//    Find a room (Chambre) associated with the reservation.
//    Ensure the room is available for reservation.
//    Mark the reservation as valid (estValide = true).
//    Save the reservation

    @Autowired
    private ReservationServiceImpl reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    private Reservation testReservation;


    @BeforeEach
    void setup() {
        // Create Students with valid CINs
        Etudiant student1 = new Etudiant();
        student1.setNomEt("Ali");
        student1.setPrenomEt("Ben Salah");
        student1.setCin(12345678L);
        student1 = etudiantRepository.save(student1);

        Etudiant student2 = new Etudiant();
        student2.setNomEt("Mehdi");
        student2.setPrenomEt("Trabelsi");
        student2.setCin(87654321L);
        student2 = etudiantRepository.save(student2);

        // Create a Reservation
        testReservation = new Reservation();
        testReservation.setEstValide(false);

        Set<Etudiant> etudiants = new HashSet<>();
        etudiants.add(student1);
        etudiants.add(student2);

        testReservation.setEtudiants(etudiants);
        testReservation = reservationRepository.save(testReservation);
    }

    @Test
    void testValidateReservation_Success() {
        // Call the function
        boolean isValid = reservationService.validateReservation(testReservation.getIdReservation());

        // Assert the result
        assertTrue(isValid, "The reservation should be validated.");

        // Ensure it is updated in the database
        Reservation updatedReservation = reservationRepository.findById(testReservation.getIdReservation()).get();
        assertTrue(updatedReservation.isEstValide(), "The reservation should be marked as valid in the database.");
    }

    @Test
    void testValidateReservation_Fail_NoStudents() {
        // Remove all students from the reservation
        testReservation.setEtudiants(new HashSet<>());
        reservationRepository.save(testReservation);

        // Call the function
        boolean isValid = reservationService.validateReservation(testReservation.getIdReservation());

        // Assert failure
        assertFalse(isValid, "The reservation should not be validated if it has no students.");
    }

    @Test
    void testValidateReservation_Fail_InvalidCIN() {
        // Set invalid CIN for one student
        testReservation.getEtudiants().iterator().next().setCin(null);
        reservationRepository.save(testReservation);

        // Call the function
        boolean isValid = reservationService.validateReservation(testReservation.getIdReservation());

        // Assert failure
        assertFalse(isValid, "The reservation should not be validated if a student has an invalid CIN.");
    }




}
