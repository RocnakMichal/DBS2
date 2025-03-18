package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
}
