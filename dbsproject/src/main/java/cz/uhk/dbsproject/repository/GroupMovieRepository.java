package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.GroupMovie;
import cz.uhk.dbsproject.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMovieRepository extends JpaRepository<GroupMovie, Integer> {
    List<GroupMovie> findByGroup(UserGroup group);
}
