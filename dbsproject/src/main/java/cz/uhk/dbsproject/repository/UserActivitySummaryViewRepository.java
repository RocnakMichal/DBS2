package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.UserActivitySummaryView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivitySummaryViewRepository extends JpaRepository<UserActivitySummaryView, Integer> {

}
