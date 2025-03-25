package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics, Integer> {
    Optional<Statistics> findByMovie(Movie movie);
}
