package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String name);

    User findByEmail(String email);
}
