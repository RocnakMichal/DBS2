package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
