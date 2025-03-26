package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.BestRatedMovieView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestRatedMovieViewRepository extends JpaRepository<BestRatedMovieView, Integer> {

}
