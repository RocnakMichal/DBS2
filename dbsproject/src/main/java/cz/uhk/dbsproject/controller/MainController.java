package cz.uhk.dbsproject.controller;

import cz.uhk.dbsproject.entity.BestRatedMovieView;
import cz.uhk.dbsproject.entity.MostRecommendedMovieView;
import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.UserActivitySummaryView;
import cz.uhk.dbsproject.repository.BestRatedMovieViewRepository;
import cz.uhk.dbsproject.repository.MostRecommendedMovieViewRepository;
import cz.uhk.dbsproject.repository.UserActivitySummaryViewRepository;
import cz.uhk.dbsproject.service.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private BestRatedMovieViewRepository movieRatingViewRepository;
    @Autowired
    private MostRecommendedMovieViewRepository recoMovieViewRepository;
    @Autowired
    private UserActivitySummaryViewRepository activityViewRepository;
    @Autowired
    private MovieService movieService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        model.addAttribute("user", session.getAttribute("user"));

        List<BestRatedMovieView> bestRated = movieRatingViewRepository.findAll();
        List<MostRecommendedMovieView> mostRecommended = recoMovieViewRepository.findAll();
        List<UserActivitySummaryView> userActivity = activityViewRepository.findAll();

        List<Movie> allMovies = movieService.getAllMovies();
        Collections.shuffle(allMovies);
        List<Movie> recommendedMovies = allMovies.stream()
                .limit(5)
                .toList();

        model.addAttribute("bestRated", bestRated);
        model.addAttribute("mostRecommended", mostRecommended);
        model.addAttribute("userActivity", userActivity);
        model.addAttribute("recommendedMovies", recommendedMovies);

        return "index";
    }
}