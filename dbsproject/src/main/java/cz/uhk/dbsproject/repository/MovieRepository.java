package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.Movie;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    @Transactional
    List<Movie> findByTitleContainingIgnoreCase(String title);

}
