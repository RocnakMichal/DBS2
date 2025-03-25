package cz.uhk.dbsproject.service;

import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.entity.Rating;
import cz.uhk.dbsproject.entity.Statistics;
import cz.uhk.dbsproject.repository.MovieRepository;
import cz.uhk.dbsproject.repository.RatingRepository;
import cz.uhk.dbsproject.repository.StatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private LogService logService;
    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;
    private final StatisticsRepository statisticsRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository, RatingRepository ratingRepository, StatisticsRepository statisticsRepository) {
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
        this.statisticsRepository = statisticsRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovie(int id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie != null && movie.getStatistics() == null) {
            statisticsService.updateStatistics(movie, ratingRepository.findByMovie(movie), 0);
        }

        return movie;
    }

    public void saveMovie(Movie movie, MovieUser user) {
        movie.setCreatedAt(LocalDateTime.now());
        Movie savedMovie = movieRepository.save(movie);

        Statistics stats = new Statistics();
        stats.setMovie(savedMovie);
        stats.setCreatedAt(LocalDateTime.now());
        stats.setAvgRating(0);
        stats.setTotalRatings(0);
        stats.setTotalRecommendations(0);

        statisticsRepository.save(stats);

        logService.log(user, "Added movie: " + movie.getTitle());
    }

    public void deleteMovie(int id) {
        movieRepository.deleteById(id);
    }

    public void addRating(int movieId, MovieUser user, Rating rating) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie == null) return;

        rating.setMovie(movie);
        rating.setUser(user);
        rating.setCreatedAt(LocalDateTime.now());

        ratingRepository.save(rating);

        List<Rating> ratings = ratingRepository.findByMovie(movie);
        int totalRecommendations = 0;

        statisticsService.updateStatistics(movie, ratings, totalRecommendations);

        logService.log(user, "Rated movie '" + movie.getTitle() + "' with " + rating.getValue());
    }

    public List<Movie> getMoviesSortedByTitle() {
        List<Movie> movies = movieRepository.findAll();
        movies.sort(Comparator.comparing(Movie::getTitle));
        return movies;
    }

    public Movie getMovieById(int id) {
        return movieRepository.findById(id).orElse(null);
    }
}