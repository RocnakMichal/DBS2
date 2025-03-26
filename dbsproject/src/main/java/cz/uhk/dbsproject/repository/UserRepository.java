package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.MovieUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<MovieUser, Integer> {
    MovieUser findByEmail(String email);
}
