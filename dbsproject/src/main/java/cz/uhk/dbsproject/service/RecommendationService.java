package cz.uhk.dbsproject.service;

import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.entity.Recommendation;
import cz.uhk.dbsproject.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    public void recommendMovie(MovieUser user, Movie movie) {
        Recommendation recommendation = new Recommendation();
        recommendation.setMovieUser(user);
        recommendation.setRecommendedMovie(movie);
        recommendation.setCreatedAt(LocalDateTime.now());
        recommendationRepository.save(recommendation);
    }

    public List<Recommendation> getByUser(MovieUser user) {
        return recommendationRepository.findByMovieUser(user);
    }

    public List<Recommendation> getAll() {
        return recommendationRepository.findAll();
    }
}