package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Integer> {
}
