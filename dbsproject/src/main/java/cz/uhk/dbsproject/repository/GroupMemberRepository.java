package cz.uhk.dbsproject.repository;

import cz.uhk.dbsproject.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Integer> {
}
