package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpfoyer.entities.Foyer;
import tn.esprit.tpfoyer.repositories.FoyerRepository;
import tn.esprit.tpfoyer.services.FoyerServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Rollback
public class FoyerServiceImplTest {
    @Autowired
    private FoyerRepository foyerRepository;

    @Autowired
    private FoyerServiceImpl foyerService;

    @BeforeEach
    void cleanDatabase() {
        foyerRepository.deleteAll(); // Ensure database is empty before running tests
    }

    @Test
    void testAddFoyer() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer Test");
        foyer.setCapaciteFoyer(100);

        Foyer savedFoyer = foyerService.addFoyer(foyer);
        assertNotNull(savedFoyer);
        assertTrue(savedFoyer.getIdFoyer() > 0);
    }

    @Test
    void testRetrieveAllFoyers() {
        foyerService.addFoyer(new Foyer(0L, "Foyer A", 200));
        foyerService.addFoyer(new Foyer(0L, "Foyer B", 300));

        List<Foyer> foyers = foyerService.retrieAllFoyers();
        assertEquals(2, foyers.size());
    }

    @Test
    void testRetrieveFoyer() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer XYZ");
        foyer.setCapaciteFoyer(150);

        Foyer savedFoyer = foyerService.addFoyer(foyer);
        Foyer retrievedFoyer = foyerService.retrieFoyer(savedFoyer.getIdFoyer());

        assertNotNull(retrievedFoyer);
        assertEquals("Foyer XYZ", retrievedFoyer.getNomFoyer());
    }

    @Test
    void testUpdateFoyer() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Old Name");
        foyer.setCapaciteFoyer(120);

        Foyer savedFoyer = foyerService.addFoyer(foyer);
        savedFoyer.setNomFoyer("New Name");
        savedFoyer.setCapaciteFoyer(200);

        Foyer updatedFoyer = foyerService.updateFoyer(savedFoyer);
        assertEquals("New Name", updatedFoyer.getNomFoyer());
        assertEquals(200, updatedFoyer.getCapaciteFoyer());
    }

    @Test
    void testRemoveFoyer() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer to Delete");
        foyer.setCapaciteFoyer(180);

        Foyer savedFoyer = foyerService.addFoyer(foyer);
        foyerService.removeFoyer(savedFoyer.getIdFoyer());

        assertFalse(foyerRepository.findById(savedFoyer.getIdFoyer()).isPresent());
    }
}
