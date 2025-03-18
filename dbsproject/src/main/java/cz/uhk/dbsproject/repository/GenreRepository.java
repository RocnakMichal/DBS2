package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GenreRepository extends JpaRepository<Genre, Integer> {
}
