package cz.uhk.dbsproject.service;

import cz.uhk.dbsproject.entity.GroupMember;
import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.entity.UserGroup;
import cz.uhk.dbsproject.repository.GroupMemberRepository;
import cz.uhk.dbsproject.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private UserGroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    public List<UserGroup> getAllGroups() {
        return groupRepository.findAll();
    }

    public UserGroup getGroupById(int id) {
        return groupRepository.findById(id).orElse(null);
    }

    public UserGroup createGroup(String name, MovieUser owner) {
        UserGroup group = new UserGroup();
        group.setName(name);
        group.setOwner(owner);
        group.setCreatedAt(LocalDateTime.now());
        groupRepository.save(group);

        // auto join the owner
        GroupMember gm = new GroupMember();
        gm.setGroup(group);
        gm.setUser(owner);
        gm.setJoinedAt(LocalDateTime.now());
        groupMemberRepository.save(gm);

        return group;
    }

    public void joinGroup(UserGroup group, MovieUser user) {
        boolean alreadyMember = groupMemberRepository.findByGroupAndMovieUser(group, user).isPresent();
        if (!alreadyMember) {
            GroupMember gm = new GroupMember();
            gm.setGroup(group);
            gm.setUser(user);
            gm.setJoinedAt(LocalDateTime.now());
            groupMemberRepository.save(gm);
        }
    }

    public void leaveGroup(UserGroup group, MovieUser user) {
        groupMemberRepository.findByGroupAndMovieUser(group, user)
                .ifPresent(groupMemberRepository::delete);

        int remainingMembers = group.getGroupMembers().size();

        if (remainingMembers == 0) {
            groupRepository.delete(group);
        }
    }

    public boolean isMember(UserGroup group, MovieUser user) {
        return groupMemberRepository.findByGroupAndMovieUser(group, user).isPresent();
    }

    public void save(UserGroup group) {
        groupRepository.save(group);
    }

    public List<MovieUser> getUsersInGroup(UserGroup group) {
        return group.getGroupMembers().stream()
                .map(GroupMember::getUser)
                .toList();
    }
}