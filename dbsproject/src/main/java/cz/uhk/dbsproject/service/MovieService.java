package cz.uhk.dbsproject.service;

import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.entity.Rating;
import cz.uhk.dbsproject.repository.MovieRepository;
import cz.uhk.dbsproject.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository, RatingRepository ratingRepository) {
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovie(int id) {
        return movieRepository.findById(id).orElse(null);
    }

    public Movie saveMovie(Movie movie) {
        if (movie.getCreatedAt() == null) {
            movie.setCreatedAt(LocalDateTime.now());
        }
        return movieRepository.save(movie);
    }

    public void deleteMovie(int id) {
        movieRepository.deleteById(id);
    }

    public void addRating(int movieId, MovieUser user, Rating rating) {
        Movie movie = movieRepository.findById(movieId).orElseThrow();
        rating.setMovie(movie);
        rating.setUser(user);
        rating.setCreatedAt(LocalDateTime.now());
        ratingRepository.save(rating);
    }

    public List<Movie> getMoviesSortedByTitle() {
        List<Movie> movies = movieRepository.findAll();
        movies.sort(Comparator.comparing(Movie::getTitle));
        return movies;
    }

}
