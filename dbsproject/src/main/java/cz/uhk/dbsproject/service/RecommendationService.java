package cz.uhk.dbsproject.service;

import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.entity.Recommendation;
import cz.uhk.dbsproject.repository.RecommendationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;
    @Autowired StatisticsService statisticsService;

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

    public void toggleRecommendation(Movie movie, MovieUser user) {
        Optional<Recommendation> existing = recommendationRepository.findByRecommendedMovieAndMovieUser(movie, user);

        if (existing.isPresent()) {
            recommendationRepository.delete(existing.get());
        } else {
            Recommendation recommendation = new Recommendation();
            recommendation.setRecommendedMovie(movie);
            recommendation.setMovieUser(user);
            recommendation.setCreatedAt(LocalDateTime.now());
            recommendationRepository.save(recommendation);
        }

        statisticsService.updateStats(movie);
    }

    public boolean isRecommendedByUser(MovieUser user, Movie movie) {
        return recommendationRepository.findByRecommendedMovieAndMovieUser(movie, user).isPresent();
    }

    public Map<Movie, Long> getGroupRecommendationScores(List<MovieUser> users) {
        List<Recommendation> recs = recommendationRepository.findByMovieUserIn(users);

        return recs.stream()
                .collect(Collectors.groupingBy(Recommendation::getRecommendedMovie, Collectors.mapping(
                        Recommendation::getMovieUser, Collectors.collectingAndThen(Collectors.toSet(), set -> (long) set.size())
                )));
    }

    public List<Recommendation> getByMovie(Movie movie) {
        return recommendationRepository.findByRecommendedMovie(movie);
    }
}