package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.GroupMovie;
import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMovieRepository extends JpaRepository<GroupMovie, Integer> {
    List<GroupMovie> findByGroup(UserGroup group);
    Optional<GroupMovie> findByGroupAndMovie(UserGroup group, Movie movie);
    void deleteByMovie(Movie movie);
    void deleteByGroupAndMovie(UserGroup group, Movie movie);
}
