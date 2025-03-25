package cz.uhk.dbsproject.controller;

import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.entity.Recommendation;
import cz.uhk.dbsproject.service.LogService;
import cz.uhk.dbsproject.service.MovieService;
import cz.uhk.dbsproject.service.RecommendationService;
import cz.uhk.dbsproject.service.StatisticsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private LogService logService;

    @GetMapping("/recommendations")
    public String viewRecommendations(HttpSession session, Model model) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<Recommendation> recommendations = recommendationService.getByUser(user);
        model.addAttribute("recommendations", recommendations);
        return "recommendations/list";
    }

    @PostMapping("/movies/{id}/recommend")
    public String recommendMovie(@PathVariable int id, HttpSession session) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Movie movie = movieService.getMovieById(id);
        recommendationService.recommendMovie(user, movie);
        statisticsService.incrementRecommendations(movie);
        logService.log(user, "💡 Recommended movie: " + movie.getTitle());

        return "redirect:/movies/detail/" + id;
    }
}