package tn.esprit.tpfoyer.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;

import tn.esprit.tpfoyer.entities.Chambre;
import tn.esprit.tpfoyer.entities.Etudiant;
import tn.esprit.tpfoyer.entities.Reservation;
import tn.esprit.tpfoyer.entities.TypeChambre;
import tn.esprit.tpfoyer.repositories.ReservationRepository;
import tn.esprit.tpfoyer.repositories.ChambreRepository;
import tn.esprit.tpfoyer.repositories.EtudiantRepository;
import tn.esprit.tpfoyer.services.ChambreServiceImpl;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test") // Assure-toi que Spring charge application-test.properties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class ChambreServiceImplTest {

    @Autowired
    private ChambreServiceImpl chambreService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    private Chambre chambre;

    @BeforeEach
    void setUp() {
        // Création d'une chambre DOUBLE (capacité max = 2)
        chambre = new Chambre();
        chambre.setTypeChambre(TypeChambre.DOUBLE);
        chambre = chambreRepository.save(chambre); // Sauvegarde en H2
    }

    @Test
    void testChambreNePeutPasDepasserCapacite() {
        // Création de deux étudiants
        Etudiant etudiant1 = etudiantRepository.save(new Etudiant());
        Etudiant etudiant2 = etudiantRepository.save(new Etudiant());

        // Ajout de deux réservations (chambre pleine)
        reservationRepository.save(new Reservation(chambre, etudiant1));
        reservationRepository.save(new Reservation(chambre, etudiant2));

        // Vérification que la chambre est pleine
        long count = reservationRepository.countByChambre(chambre);
        assertEquals(2, count, "La chambre doit être pleine avec 2 réservations.");

        // Troisième étudiant essayant de réserver
        Etudiant etudiant3 = etudiantRepository.save(new Etudiant());

        // Vérifier qu'une exception est levée
        Exception exception = assertThrows(RuntimeException.class, () -> {
            chambreService.reserverChambre(chambre, etudiant3);
        });

        // Vérifier le message de l'exception
        assertEquals("Cette chambre est déjà pleine.", exception.getMessage());
        System.out.println("Cette chambre est déjà pleine");
    }

}