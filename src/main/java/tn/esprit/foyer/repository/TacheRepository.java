package tn.esprit.foyer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.foyer.entities.Tache;
import tn.esprit.foyer.entities.TypeTache;

import java.time.LocalDate;
import java.util.List;       // Ajout de l'import pour List
import java.util.Optional;   // Ajout de l'import pour Optional


@Repository
public interface TacheRepository extends JpaRepository<Tache,Long> {

    /**
     * Méthode existante pour calculer la somme des coûts pour un étudiant donné dans une période.
     */
    @Query("select sum(t.tarifHoraire*t.duree) from Tache t where t.dateTache " +
            "between :t1 and :t2 and t.etudiant.idEtudiant=:idEtudiant")
    Float sommeTacheAnneeEncours(@Param("t1") LocalDate t1,
                                 @Param("t2")LocalDate t2,
                                 @Param("idEtudiant")Long idEtudiant);


    // --- Nouvelles requêtes pour les statistiques (basées sur tâches assignées, sans DTO, sans statut) ---

    /**
     * Calcule le coût total des tâches ASSIGNEES dans une période.
     * Utilise COALESCE pour retourner 0.0 si aucune tâche n'est trouvée ou si les valeurs sont nulles.
     */
    @Query("SELECT COALESCE(SUM(t.duree * t.tarifHoraire), 0.0) " +
            "FROM Tache t " +
            "WHERE t.etudiant IS NOT NULL " + // Seulement les tâches assignées
            "AND t.dateTache BETWEEN :startDate AND :endDate")
    Double calculateTotalCostAssignedTasks(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Calcule le coût moyen des tâches ASSIGNEES dans une période.
     * Retourne Optional<Double> car AVG peut être null si aucune tâche ne correspond.
     */
    @Query("SELECT AVG(t.duree * t.tarifHoraire) " +
            "FROM Tache t " +
            "WHERE t.etudiant IS NOT NULL " + // Seulement les tâches assignées
            "AND t.dateTache BETWEEN :startDate AND :endDate")
    Optional<Double> calculateAverageCostAssignedTasks(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Compte le nombre de tâches ASSIGNEES dans une période.
     */
    @Query("SELECT COUNT(t) " +
            "FROM Tache t " +
            "WHERE t.etudiant IS NOT NULL " + // Seulement les tâches assignées
            "AND t.dateTache BETWEEN :startDate AND :endDate")
    long countAssignedTasks(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Calcule le coût total des tâches ASSIGNEES par TypeTache.
     * Retourne une liste de tableaux d'objets [TypeTache, Double].
     */
    @Query("SELECT t.typeTache, COALESCE(SUM(t.duree * t.tarifHoraire), 0.0) " +
            "FROM Tache t " +
            "WHERE t.etudiant IS NOT NULL " + // Seulement les tâches assignées
            "AND t.dateTache BETWEEN :startDate AND :endDate " +
            "GROUP BY t.typeTache")
    List<Object[]> calculateTotalCostAssignedTasksByTypeRaw(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    /**
     * Calcule le coût total des tâches ASSIGNEES par Bloc.
     * Retourne une liste de tableaux d'objets [String nomBloc, Double totalCost].
     * Attention: La jointure pour trouver le Bloc via Etudiant->Reservation->Chambre->Bloc est complexe
     * et dépend de la structure exacte et de l'existence des données liées.
     * Adaptez si une relation plus directe Tache->Bloc existe.
     */
    @Query("SELECT b.nomBloc, COALESCE(SUM(t.duree * t.tarifHoraire), 0.0) " +
            "FROM Tache t " +
            "JOIN t.etudiant e " + // Assure t.etudiant IS NOT NULL
            "LEFT JOIN e.reservations res " + // Jointures pour atteindre le Bloc
            "LEFT JOIN res.chambre ch " +
            "LEFT JOIN ch.bloc b " +
            "WHERE t.dateTache BETWEEN :startDate AND :endDate " +
            "GROUP BY b.nomBloc") // Groupe par nom de bloc
    List<Object[]> calculateTotalCostAssignedTasksByBlocRaw(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}