package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
}
