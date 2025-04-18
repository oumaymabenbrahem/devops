package tn.esprit.tpfoyer.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entities.Etudiant;
import tn.esprit.tpfoyer.entities.Tache;
import tn.esprit.tpfoyer.entities.TypeEtudiant;
import tn.esprit.tpfoyer.entities.TypeTache;
import tn.esprit.tpfoyer.repositories.EtudiantRepository;
import tn.esprit.tpfoyer.repositories.TacheRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests avancés pour TacheServiceImpl
 * Contient deux tests : un avec Mockito et un avec JUnit
 */
@ExtendWith(MockitoExtension.class)
class TacheServiceImplTest {

    @Mock
    private TacheRepository tacheRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private TacheServiceImpl tacheService;

    private Etudiant etudiant;
    private List<Tache> taches;

    @BeforeEach
    void setUp() {
        // Create a test student
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEt("Dupont");
        etudiant.setPrenomEt("Jean");
        etudiant.setEcole("ESPRIT");
        etudiant.setTypeEtudiant(TypeEtudiant.ORDINAIRE);
        etudiant.setTaches(new HashSet<>());

        // Create test tasks
        taches = new ArrayList<>();
        taches.add(new Tache(1L, LocalDate.now(), 5, 10.0f, TypeTache.MENAGER));
        taches.add(new Tache(2L, LocalDate.now().plusDays(1), 3, 15.0f, TypeTache.BRICOLAGE));
        taches.add(new Tache(3L, LocalDate.now().plusDays(2), 8, 12.5f, TypeTache.JARDINAGE));
    }

    /**
     * Test avancé avec Mockito pour l'affectation de tâches à un étudiant
     * Ce test utilise Mockito pour simuler le comportement des repositories
     */
    @Test
    void testAddTasksAndAffectToEtudiant() {
        // Given
        String nomEt = "Dupont";
        String prenomEt = "Jean";

        System.out.println("=== TEST AVANCÉ: Ajout de tâches et affectation à un étudiant ===\n");
        System.out.println("Étudiant: " + nomEt + " " + prenomEt);
        System.out.println("Nombre de tâches à affecter: " + taches.size());

        // Afficher les détails des tâches
        System.out.println("\nDétails des tâches à affecter:");
        for (int i = 0; i < taches.size(); i++) {
            Tache t = taches.get(i);
            System.out.println("Tâche " + (i+1) + ": Type=" + t.getTypeTache() +
                    ", Durée=" + t.getDuree() + "h, Tarif=" + t.getTarifHoraire() + "€/h");
        }

        // Mock the repository behavior
        when(etudiantRepository.findByNomAndPrenom(nomEt, prenomEt)).thenReturn(List.of(etudiant));
        when(tacheRepository.save(any(Tache.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        System.out.println("\nExécution de l'affectation des tâches...");
        List<Tache> result = tacheService.addTasksAndAffectToEtudiant(taches, nomEt, prenomEt);

        // Then
        assertEquals(3, result.size(), "Should return all three tasks");

        // Verify that the student was looked up
        verify(etudiantRepository).findByNomAndPrenom(nomEt, prenomEt);
        System.out.println("✅ Étudiant trouvé avec succès");

        // Verify that save was called for each task
        verify(tacheRepository, times(3)).save(any(Tache.class));
        System.out.println("✅ Les 3 tâches ont été sauvegardées");

        // Verify that each task is assigned to the student
        boolean allTasksAssigned = true;
        for (Tache tache : result) {
            if (tache.getEtudiant() != etudiant) {
                allTasksAssigned = false;
                break;
            }
        }

        if (allTasksAssigned) {
            System.out.println("✅ Toutes les tâches ont été correctement affectées à l'étudiant");
        } else {
            System.out.println("❌ Certaines tâches n'ont pas été correctement affectées");
        }

        System.out.println("\n✅ TEST RÉUSSI: Affectation des tâches à l'étudiant\n");
    }



    /**
     * Test avancé avec JUnit pour calculer le coût total de toutes les tâches
     * Ce test utilise principalement JUnit pour vérifier les calculs métier
     */
    @Test
    void testCalculateTotalCostOfAllTasks() {
        // Given
        System.out.println("=== TEST AVANCÉ: Calcul du coût total de toutes les tâches ===\n");

        // Afficher les détails des tâches
        System.out.println("Détails des tâches:");
        float expectedTotalCost = 0;

        for (int i = 0; i < taches.size(); i++) {
            Tache t = taches.get(i);
            float taskCost = t.getDuree() * t.getTarifHoraire();
            expectedTotalCost += taskCost;

            System.out.println("Tâche " + (i+1) + ": Type=" + t.getTypeTache() +
                    ", Durée=" + t.getDuree() + "h, Tarif=" + t.getTarifHoraire() +
                    "€/h, Coût=" + taskCost + "€");
        }

        // When - Calculer le coût total
        System.out.println("\nCalcul du coût total de toutes les tâches...");
        float totalCost = 0;
        for (Tache t : taches) {
            totalCost += t.getDuree() * t.getTarifHoraire();
        }

        // Then
        System.out.println("Coût total calculé: " + totalCost + "€");
        assertEquals(expectedTotalCost, totalCost, 0.01,
                "Le coût total doit être la somme des coûts individuels");

        System.out.println("\n✅ TEST RÉUSSI: Calcul du coût total de toutes les tâches\n");
    }
}
