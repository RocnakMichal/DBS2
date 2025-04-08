package cz.uhk.dbsproject.service;

import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.Rating;
import cz.uhk.dbsproject.entity.Statistics;
import cz.uhk.dbsproject.repository.RatingRepository;
import cz.uhk.dbsproject.repository.RecommendationRepository;
import cz.uhk.dbsproject.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private RecommendationRepository recommendationRepository;
    private final StatisticsRepository statisticsRepository;

    @Autowired
    public StatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    public Statistics getByMovie(Movie movie) {
        return statisticsRepository.findByMovie(movie).orElse(null);
    }

    public List<Statistics> getAllStatistics() {
        return statisticsRepository.findAll();
    }

    public void updateStats(Movie movie) {
        List<Rating> ratings = ratingRepository.findByMovie(movie);
        int totalRecommendations = recommendationRepository.findByRecommendedMovie(movie).size();
        updateStatistics(movie, ratings, totalRecommendations);
    }

    public void updateStatistics(Movie movie, List<Rating> ratings, int totalRecommendations) {
        Statistics stats = statisticsRepository.findByMovie(movie).orElse(null);

        double avg = ratings.stream()
                .mapToDouble(Rating::getValue)
                .average()
                .orElse(0.0);

        int count = ratings.size();

        if (stats == null) {
            stats = new Statistics();
            stats.setMovie(movie);
            stats.setCreatedAt(LocalDateTime.now());
        }

        stats.setAvgRating(avg);
        stats.setTotalRatings(count);
        stats.setTotalRecommendations(totalRecommendations); // Placeholder or real value

        statisticsRepository.save(stats);
    }

    public void incrementRecommendations(Movie movie) {
        Statistics stats = statisticsRepository.findByMovie(movie)
                .orElseGet(() -> {
                    Statistics s = new Statistics();
                    s.setMovie(movie);
                    s.setCreatedAt(LocalDateTime.now());
                    return s;
                });

        stats.setTotalRecommendations(stats.getTotalRecommendations() + 1);
        statisticsRepository.save(stats);
    }
}