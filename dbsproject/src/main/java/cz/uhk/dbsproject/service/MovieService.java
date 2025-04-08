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
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;
    private final StatisticsRepository statisticsRepository;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private LogService logService;

    @Autowired
    public MovieService(MovieRepository movieRepository, RatingRepository ratingRepository, StatisticsRepository statisticsRepository) {
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
        this.statisticsRepository = statisticsRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> getMoviesSortedByTitle() {
        List<Movie> movies = movieRepository.findAll();
        movies.sort(Comparator.comparing(Movie::getTitle));
        return movies;
    }

    public Movie getMovie(int id) {
        Movie movie = movieRepository.findById(id).orElse(null);
        if (movie != null && movie.getStatistics() == null) {
            statisticsService.updateStatistics(movie, ratingRepository.findByMovie(movie), 0);
        }
        return movie;
    }

    public Movie getMovieById(int id) {
        return movieRepository.findById(id).orElse(null);
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

    public void addRating(int id, MovieUser user, Rating rating) {
        Movie movie = getMovieById(id);
        if (movie != null) {
            addOrUpdateRating(movie, user, rating);
        }
    }

    public void addOrUpdateRating(Movie movie, MovieUser user, Rating rating) {
        Rating existing = ratingRepository.findByMovieAndMovieUser(movie, user).orElse(null);

        if (existing != null) {
            existing.setComment(rating.getComment());
            existing.setValue(rating.getValue());
            existing.setCreatedAt(LocalDateTime.now());
            ratingRepository.save(existing);
        } else {
            rating.setMovie(movie);
            rating.setMovieUser(user);
            rating.setCreatedAt(LocalDateTime.now());
            ratingRepository.save(rating);
        }

        statisticsService.updateStats(movie);
    }

    public void deleteRating(Movie movie, MovieUser user) {
        ratingRepository.findByMovieAndMovieUser(movie, user)
                .ifPresent(ratingRepository::delete);

        statisticsService.updateStats(movie);
    }

    public Optional<Rating> getUserRating(Movie movie, MovieUser user) {
        return ratingRepository.findByMovieAndMovieUser(movie, user);
    }
}