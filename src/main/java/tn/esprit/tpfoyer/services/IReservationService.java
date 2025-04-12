package tn.esprit.tpfoyer.services;

import tn.esprit.tpfoyer.entities.Foyer;
import tn.esprit.tpfoyer.entities.Reservation;

import java.util.List;

public interface IReservationService
{
    List<Reservation> retrieAllReservations();
    Reservation addReservation(Reservation r);
    Reservation updateReservation(Reservation r);
    Reservation retrieReservation(Long idReservation);
    void removeReservation(Long idReservation);
}
