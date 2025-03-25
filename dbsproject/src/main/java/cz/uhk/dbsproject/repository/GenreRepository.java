package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Optional<Genre> findByNameIgnoreCase(String name);
}
