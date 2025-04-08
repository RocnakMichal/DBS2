package cz.uhk.dbsproject.controller;

import cz.uhk.dbsproject.entity.Movie;
import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.entity.Recommendation;
import cz.uhk.dbsproject.entity.UserGroup;
import cz.uhk.dbsproject.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

            Movie currentSuggestion = group.getSuggestedMovie();
            List<MovieUser> users = groupService.getUsersInGroup(group);

            boolean isSuggestionValid = false;

            if (currentSuggestion != null) {
                isSuggestionValid = recommendationService.getByMovie(currentSuggestion).stream()
                        .anyMatch(r -> users.contains(r.getMovieUser()));
            }

            if (!isSuggestionValid && session.getAttribute("suggestion-scores") != null) {
                rerollMovieSuggestion(id, session, model);
            }

            if (currentSuggestion != null) {
                List<String> recommenderNames = recommendationService.getByMovie(currentSuggestion).stream()
                        .filter(r -> users.contains(r.getMovieUser()))
                        .map(r -> r.getMovieUser().getName())
                        .distinct()
                        .toList();

                model.addAttribute("suggestionRecommenders", recommenderNames);
            }
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
    public String addMovieToGroup(@PathVariable int id, @RequestParam int movieId, HttpSession session, Model model) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        UserGroup group = groupService.getGroupById(id);
        Movie movie = movieService.getMovieById(movieId);

        if (!groupService.isMember(group, user)) {
            return "redirect:/groups/" + id + "?error=unauthorized";
        }

        boolean added = groupMovieService.addMovieToGroup(group, movie);
        if (!added) {
            model.addAttribute("error", "This movie is already added to the group.");
            model.addAttribute("group", group);
            model.addAttribute("isMember", true);
            model.addAttribute("allMovies", movieService.getAllMovies());
            return "groups/detail";
        }

        return "redirect:/groups/" + id;
    }

    @PostMapping("/{groupId}/remove-movie")
    public String removeMovieFromGroup(@PathVariable int groupId, @RequestParam int movieId, HttpSession session) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        UserGroup group = groupService.getGroupById(groupId);
        Movie movie = movieService.getMovieById(movieId);

        if (!groupService.isMember(group, user)) {
            return "redirect:/groups/" + groupId + "?error=unauthorized";
        }

        groupMovieService.removeMovieFromGroup(group, movie);
        return "redirect:/groups/" + groupId;
    }

    @GetMapping("/{id}/suggest")
    public String suggestMovie(@PathVariable int id, HttpSession session, Model model) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        UserGroup group = groupService.getGroupById(id);
        if (!groupService.isMember(group, user)) return "redirect:/groups";

        List<MovieUser> users = groupService.getUsersInGroup(group);

        Map<Movie, Long> scores = recommendationService.getGroupRecommendationScores(users);
        int groupSize = users.size();

        // Store data for reroll
        session.setAttribute("suggestion-tier", groupSize); // max tier = everyone recommended
        session.setAttribute("suggestion-scores", scores);
        session.setAttribute("suggestion-groupId", id);

        return rerollMovieSuggestion(id, session, model);
    }

    private String rerollMovieSuggestion(int groupId, HttpSession session, Model model) {
        UserGroup group = groupService.getGroupById(groupId);
        List<MovieUser> users = groupService.getUsersInGroup(group);

        List<Recommendation> recs = users.stream()
                .flatMap(user -> recommendationService.getByUser(user).stream())
                .toList();

        Map<Movie, Long> scores = recs.stream()
                .collect(Collectors.groupingBy(
                        Recommendation::getRecommendedMovie,
                        Collectors.mapping(
                                Recommendation::getMovieUser,
                                Collectors.collectingAndThen(Collectors.toSet(), set -> (long) set.size())
                        )
                ));

        Integer tierFromSession = (Integer) session.getAttribute("suggestion-tier");
        final int tier;
        if (tierFromSession == null) {
            tier = scores.values().stream().max(Long::compare).orElse(1L).intValue();
            session.setAttribute("suggestion-tier", tier);
        } else {
            tier = tierFromSession;
        }

        List<Movie> currentTier = scores.entrySet().stream()
                .filter(e -> e.getValue() == tier)
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(ArrayList::new));

        Movie suggestion;

        if (!currentTier.isEmpty()) {
            Collections.shuffle(currentTier);
            suggestion = currentTier.get(0);
        } else if (tier > 1) {
            session.setAttribute("suggestion-tier", tier - 1);
            return rerollMovieSuggestion(groupId, session, model);
        } else {
            List<Movie> allMovies = movieService.getAllMovies();
            Collections.shuffle(allMovies);
            suggestion = allMovies.get(0);
        }

        group.setSuggestedMovie(suggestion);
        groupService.save(group);

        Set<Integer> groupUserIds = users.stream()
                .map(MovieUser::getId)
                .collect(Collectors.toSet());

        List<Recommendation> recommendationList = recommendationService.getByMovie(suggestion).stream()
                .filter(r -> groupUserIds.contains(r.getMovieUser().getId()))
                .toList();

        List<String> recommenderNames = recommendationList.stream()
                .map(r -> r.getMovieUser().getName())
                .distinct()
                .toList();

        model.addAttribute("group", group);
        model.addAttribute("isMember", true);
        model.addAttribute("allMovies", movieService.getAllMovies());
        model.addAttribute("suggestionRecommenders", recommenderNames);
        session.setAttribute("suggestion-scores", scores);

        return "groups/detail";
    }

    @PostMapping("/{id}/reroll")
    public String reroll(@PathVariable int id, HttpSession session, Model model) {
        return rerollMovieSuggestion(id, session, model);
    }
}