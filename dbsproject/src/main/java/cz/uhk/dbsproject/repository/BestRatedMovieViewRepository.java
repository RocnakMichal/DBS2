package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestRatedMovieViewRepository extends JpaRepository<UserGroup, Integer> {

}
