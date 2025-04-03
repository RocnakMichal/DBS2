package cz.uhk.dbsproject.controller;

import cz.uhk.dbsproject.entity.Genre;
import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.entity.Rating;
import cz.uhk.dbsproject.repository.MovieRepository;
import cz.uhk.dbsproject.service.GenreService;
import cz.uhk.dbsproject.service.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    private final MovieService movieService;
    private final GenreService genreService;



    @Autowired
    public MovieController(MovieService movieService, GenreService genreService) {
        this.movieService = movieService;
        this.genreService = genreService;
    }

    // Show all movies
    @GetMapping
    public String showAllMovies(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("user", session.getAttribute("user"));
        return "movies";
    }

    @GetMapping("/search")
    @ResponseBody
    public List<Movie> searchMovies(@RequestParam("query") String query) {
        return movieRepository.findByTitleContainingIgnoreCase(query);
    }

    // Movie detail
    @GetMapping("/detail/{id}")
    public String movieDetail(@PathVariable int id, HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        Movie movie = movieService.getMovie(id);
        if (movie == null) return "redirect:/movies";

        model.addAttribute("movie", movie);
        model.addAttribute("user", session.getAttribute("user"));
        return "movie-detail";
    }

    // Show add form
    @GetMapping("/add")
    public String showAddMovieForm(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        model.addAttribute("movie", new Movie());

        return "add-movie";
    }

    // Submit new movie
    @PostMapping("/add")
    public String addMovie(@RequestParam String title, @RequestParam(required = false) Integer genreId,
                           @RequestParam(required = false) String newGenre, @RequestParam String director,
                           @RequestParam int releaseYear, @RequestParam String description,
                           @RequestParam(required = false) String imageUrl, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDirector(director);
        movie.setReleaseYear(releaseYear);
        movie.setDescription(description);
        movie.setImageUrl(imageUrl);

        Genre genre = null;

        if (newGenre != null && !newGenre.isBlank()) {
            // Check if genre with that name exists
            genre = genreService.findByName(newGenre.trim()).orElse(null);
            if (genre == null) {
                // If not, create and save new genre
                genre = new Genre();
                genre.setName(newGenre.trim());
                genre = genreService.save(genre);
            }
        } else if (genreId != null) {
            genre = genreService.getGenreById(genreId);
        }

        movie.setGenre(genre);

        MovieUser user = (MovieUser) session.getAttribute("user");
        movieService.saveMovie(movie, user);

        return "redirect:/movies";
    }

    // Delete movie
    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        movieService.deleteMovie(id);
        return "redirect:/movies";
    }

    // Show rate form
    @GetMapping("/rate/{id}")
    public String showRateMovieForm(@PathVariable int id, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        Movie movie = movieService.getMovie(id);
        if (movie == null) return "redirect:/movies";

        model.addAttribute("movie", movie);
        model.addAttribute("rating", new Rating());
        return "rate-movie";
    }

    // Submit rating
    @PostMapping("/rate/{id}")
    public String rateMovie(@PathVariable int id, @ModelAttribute Rating rating, HttpSession session) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        movieService.addRating(id, user, rating);
        return "redirect:/movies/detail/" + id;
    }

    // Placeholder for recommended
    @GetMapping("/recommended")
    public String recommendedMovies(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        model.addAttribute("movies", movieService.getAllMovies()); // Replace with real logic
        return "recommended";
    }
}
