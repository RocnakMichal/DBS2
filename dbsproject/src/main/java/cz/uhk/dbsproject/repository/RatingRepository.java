package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Optional<Rating> findByMovieAndMovieUser(Movie movie, MovieUser user);
    List<Rating> findByMovie(Movie movie);
}
