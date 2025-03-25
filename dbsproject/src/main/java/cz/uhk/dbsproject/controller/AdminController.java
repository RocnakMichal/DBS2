package cz.uhk.dbsproject.controller;

import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.repository.BestRatedMovieViewRepository;
import cz.uhk.dbsproject.repository.MostRecommendedMovieViewRepository;
import cz.uhk.dbsproject.repository.UserActivitySummaryViewRepository;
import cz.uhk.dbsproject.service.LogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BestRatedMovieViewRepository movieRatingViewRepository;
    @Autowired
    private MostRecommendedMovieViewRepository recoMovieViewRepository;
    @Autowired
    private UserActivitySummaryViewRepository activityViewRepository;
    private final LogService logService;

    @Autowired
    public AdminController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (!isAdmin(session)) return "redirect:/";
        return "admin/dashboard";
    }

    @GetMapping("/logs")
    public String viewLogs(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/";

        model.addAttribute("logs", logService.getAllLogs());
        return "admin/logs";
    }

    private boolean isAdmin(HttpSession session) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    @GetMapping("/views")
    public String showViews(Model model) {
        model.addAttribute("bestRated", movieRatingViewRepository.findAll());
        model.addAttribute("mostRecommended", recoMovieViewRepository.findAll());
        model.addAttribute("userActivity", activityViewRepository.findAll());
        return "admin/views";
    }
}