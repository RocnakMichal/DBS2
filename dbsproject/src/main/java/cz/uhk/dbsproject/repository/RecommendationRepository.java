package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {
    Optional<Recommendation> findByRecommendedMovieAndMovieUser(Movie movie, MovieUser user);
    List<Recommendation> findByMovieUser(MovieUser user);
    List<Recommendation> findByRecommendedMovie(Movie movie);
    List<Recommendation> findByMovieUserIn(List<MovieUser> users);
}
