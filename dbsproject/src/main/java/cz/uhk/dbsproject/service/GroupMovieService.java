package cz.uhk.dbsproject.service;

import cz.uhk.dbsproject.entity.GroupMovie;
import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.UserGroup;
import cz.uhk.dbsproject.repository.GroupMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupMovieService {

    @Autowired
    private GroupMovieRepository groupMovieRepo;

    public void addMovieToGroup(UserGroup group, Movie movie) {
        GroupMovie gm = new GroupMovie();
        gm.setGroup(group);
        gm.setMovie(movie);
        gm.setCreatedAt(LocalDateTime.now());
        groupMovieRepo.save(gm);
    }

    public List<GroupMovie> getMoviesByGroup(UserGroup group) {
        return groupMovieRepo.findByGroup(group);
    }

    public void removeGroupMovie(int id) {
        groupMovieRepo.deleteById(id);
    }
}