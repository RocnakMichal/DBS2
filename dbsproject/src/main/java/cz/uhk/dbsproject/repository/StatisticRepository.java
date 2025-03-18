package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticRepository extends JpaRepository<Statistic, Integer> {
}
