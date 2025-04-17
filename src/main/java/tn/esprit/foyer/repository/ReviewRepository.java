package tn.esprit.foyer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.foyer.entities.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

}
