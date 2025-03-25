package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticRepository extends JpaRepository<Statistics, Integer> {
}
