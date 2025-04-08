package cz.uhk.dbsproject.controller;

import cz.uhk.dbsproject.entity.*;
import cz.uhk.dbsproject.repository.MovieRepository;
import cz.uhk.dbsproject.service.GenreService;
import cz.uhk.dbsproject.service.MovieService;
import cz.uhk.dbsproject.service.RecommendationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private RecommendationService recommendationService;

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
       // if (session.getAttribute("user") == null) return "redirect:/login";

        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("user", session.getAttribute("user"));
        return "movies";
    }

    // Movie detail
    @GetMapping("/detail/{id}")
    public String movieDetail(@PathVariable int id, HttpSession session, Model model) {
        MovieUser user = (MovieUser) session.getAttribute("user");

        Movie movie = movieService.getMovie(id);
        if (movie == null) return "redirect:/movies";

        model.addAttribute("movie", movie);
        model.addAttribute("user", session.getAttribute("user"));

        boolean isRecommended = false;
        if (session.getAttribute("user") == null) {
            isRecommended = recommendationService.isRecommendedByUser(user, movie);
        }
        model.addAttribute("userRecommended", isRecommended);

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
    public String addMovie(@RequestParam String title,
                           @RequestParam(required = false) Integer genreId,
                           @RequestParam(required = false) String newGenre,
                           @RequestParam String director,
                           @RequestParam int releaseYear,
                           @RequestParam String description,
                           @RequestParam("image") MultipartFile imageFile,
                           HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";

        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDirector(director);
        movie.setReleaseYear(releaseYear);
        movie.setDescription(description);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String uploadDir = "images/uploads/";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String filename = imageFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                movie.setImageUrl(filename);
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }

        Genre genre = null;
        if (newGenre != null && !newGenre.isBlank()) {
            genre = genreService.findByName(newGenre.trim()).orElse(null);
            if (genre == null) {
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
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/movies";
        }

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

    @GetMapping("/recommendations")
    public String viewRecommendations(HttpSession session, Model model) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Recommendation> recommendations = recommendationService.getByUser(user);
        model.addAttribute("recommendations", recommendations);
        return "recommendations/list";
    }

    @PostMapping("/{id}/delete-rating")
    public String deleteRating(@PathVariable int id, HttpSession session) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Movie movie = movieService.getMovieById(id);
        movieService.deleteRating(movie, user);

        return "redirect:/movies/detail/" + id;
    }

    @PostMapping("/recommend/{id}")
    public String toggleRecommendation(@PathVariable int id, HttpSession session) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Movie movie = movieService.getMovieById(id);
        recommendationService.toggleRecommendation(movie, user);

        return "redirect:/movies/detail/" + id;
    }
}
