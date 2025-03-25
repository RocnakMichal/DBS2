package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findByMovie(Movie movie);
}
