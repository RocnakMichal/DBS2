package cz.uhk.dbsproject.controller;

import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.entity.UserGroup;
import cz.uhk.dbsproject.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupMovieService groupMovieService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private LogService logService;
    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    public String listGroups(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        model.addAttribute("groups", groupService.getAllGroups());
        return "groups/list";
    }

    @GetMapping("/{id}")
    public String viewGroup(@PathVariable int id, Model model, HttpSession session) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        UserGroup group = groupService.getGroupById(id);
        if (group == null) return "redirect:/groups";

        boolean isMember = groupService.isMember(group, user);

        model.addAttribute("group", group);
        model.addAttribute("isMember", isMember);

        if (isMember) {
            model.addAttribute("allMovies", movieService.getAllMovies());
        }

        return "groups/detail";
    }

    @PostMapping("/create")
    public String createGroup(
            @RequestParam String name,
            @RequestParam(required = false) boolean isPrivate,
            @RequestParam(required = false) String joinPassword,
            HttpSession session
    ) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        UserGroup group = new UserGroup();
        group.setName(name);
        group.setOwner(user);
        group.setCreatedAt(LocalDateTime.now());
        group.setPrivate(isPrivate);
        group.setJoinPassword(isPrivate ? joinPassword : null);

        groupService.save(group);
        groupService.joinGroup(group, user);

        return "redirect:/groups";
    }

    @PostMapping("/{id}/join")
    public String joinGroup(@PathVariable int id, @RequestParam(required = false) String password, HttpSession session, Model model) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        UserGroup group = groupService.getGroupById(id);
        if (group.isPrivate()) {
            if (group.getJoinPassword() == null || !group.getJoinPassword().equals(password)) {
                logService.log(user, "Failed to join private group '" + group.getName() + "' (wrong password)");
                model.addAttribute("group", group);
                model.addAttribute("isMember", false);
                model.addAttribute("error", "Incorrect password.");
                return "groups/detail";
            }
        }

        if (!groupService.isMember(group, user)) {
            groupService.joinGroup(group, user);
            logService.log(user, "Joined group '" + group.getName() + "'");
        }

        groupService.joinGroup(group, user);
        return "redirect:/groups/" + id;
    }

    @PostMapping("/{id}/leave")
    public String leaveGroup(@PathVariable int id, HttpSession session) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        UserGroup group = groupService.getGroupById(id);
        groupService.leaveGroup(group, user);
        return "redirect:/groups";
    }

    @PostMapping("/{id}/add-movie")
    public String addMovieToGroup(@PathVariable int id, @RequestParam int movieId, HttpSession session) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        UserGroup group = groupService.getGroupById(id);
        Movie movie = movieService.getMovieById(movieId);
        groupMovieService.addMovieToGroup(group, movie);
        return "redirect:/groups/" + id;
    }

    @GetMapping("/groups/{id}/recommend")
    public String showRecommendation(@PathVariable int id, Model model, HttpSession session) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        UserGroup group = groupService.getGroupById(id);
        if (group == null) return "redirect:/groups";

        boolean isMember = groupService.isMember(group, user);

        model.addAttribute("group", group);
        model.addAttribute("allMovies", movieService.getAllMovies());
        model.addAttribute("isMember", isMember);

        Movie recommendedMovie = recommendationService.getRecommendedMovieForGroup(group);
        model.addAttribute("recommendedMovie", recommendedMovie);

        return "group-detail";
    }
}