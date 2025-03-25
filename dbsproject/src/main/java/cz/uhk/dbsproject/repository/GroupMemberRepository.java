package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.GroupMember;
import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Integer> {
    Optional<GroupMember> findByGroupAndMovieUser(UserGroup group, MovieUser user);
    List<GroupMember> findByMovieUser(MovieUser user);
}
