package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.Log;
import cz.uhk.dbsproject.entity.MovieUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findByUser(MovieUser user);
}
