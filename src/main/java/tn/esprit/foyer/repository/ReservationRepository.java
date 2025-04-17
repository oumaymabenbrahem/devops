package tn.esprit.foyer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.foyer.entities.Chambre;
import tn.esprit.foyer.entities.Reservation;
import java.time.LocalDate;
import java.util.List;



public interface ReservationRepository extends JpaRepository<Reservation,String> {

List<Reservation> findByAnneeUniversitaireBetween(LocalDate dateDebut, LocalDate d2);


    @Query("select count (c) from Chambre c join c.reservations r where r.anneeUniversitaire between :startDate and  :endDate and c.numeroChambre= :numeroChambre" +
            " and r.estValid = true")
    Integer getReservationsCurrentYear(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                     @Param("numeroChambre") Long numeroChambre);

    long countByChambreAndAnneeUniversitaire(Chambre chambre, LocalDate anneeUniversitaire);

    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.chambre.idChambre IN :chambreIds " +
            "AND r.estValid = true " +
            "AND r.anneeUniversitaire >= :dateDebut " + // Condition à adapter
            "AND r.anneeUniversitaire <= :dateFin")     // Condition à adapter
    long countValidReservationsForChambresInPeriod(@Param("chambreIds") List<Long> chambreIds,
                                                   @Param("dateDebut") LocalDate dateDebut,
                                                   @Param("dateFin") LocalDate dateFin);
}
