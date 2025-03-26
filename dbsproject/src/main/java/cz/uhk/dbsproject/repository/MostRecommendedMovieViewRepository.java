package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.MostRecommendedMovieView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MostRecommendedMovieViewRepository extends JpaRepository<MostRecommendedMovieView, Integer> {

}
