package cz.uhk.dbsproject.service;

import cz.uhk.dbsproject.entity.GroupMovie;
import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.UserGroup;
import cz.uhk.dbsproject.repository.GroupMovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupMovieService {

    @Autowired
    private GroupMovieRepository groupMovieRepo;

    @Autowired
    private GroupMovieRepository groupMovieRepository;

    public boolean isMovieInGroup(UserGroup group, Movie movie) {
        return groupMovieRepository.findByGroupAndMovie(group, movie).isPresent();
    }
    public boolean  addMovieToGroup(UserGroup group, Movie movie) {
        if (isMovieInGroup(group, movie)) {
            return false;
        }

        GroupMovie gm = new GroupMovie();
        gm.setGroup(group);
        gm.setMovie(movie);
        gm.setCreatedAt(LocalDateTime.now());
        groupMovieRepo.save(gm);
        return true;
    }

    public List<GroupMovie> getMoviesByGroup(UserGroup group) {
        return groupMovieRepo.findByGroup(group);
    }

    public void removeGroupMovie(int id) {
        groupMovieRepo.deleteById(id);
    }

    @Transactional
    public void removeMovieFromAllGroups(Movie movie) {
        groupMovieRepository.deleteByMovie(movie);
    }

    @Transactional
    public void removeMovieFromGroup(UserGroup group, Movie movie) {
        groupMovieRepository.findByGroupAndMovie(group, movie)
                .ifPresent(groupMovieRepository::delete);
    }
}