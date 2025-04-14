package cz.uhk.dbsproject.controller;

import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(HttpSession session, Model model) {
        MovieUser loggedInUser = (MovieUser) session.getAttribute("user");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        List<MovieUser> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("user", loggedInUser);
        return "users";
    }

    @GetMapping("/{id}")
    public MovieUser getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @PostMapping("/add")
    public String createUser(@RequestParam String name, @RequestParam String email) {
        MovieUser newUser = new MovieUser();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPasswordHash("test123"); // Default password for testing, will be changed later
        newUser.setRole("USER");
        userService.createUser(newUser);
        return "redirect:/users";
    }

    @PutMapping("/{id}")
    public MovieUser updateUser(@PathVariable int id, @RequestBody MovieUser updatedMovieUser) {
        return userService.updateUser(id, updatedMovieUser);
    }


    @GetMapping("/edit")
    public String editUserForm(HttpSession session, Model model) {
        MovieUser loggedInUser = (MovieUser) session.getAttribute("user");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", loggedInUser);
        return "profile-edit";
    }

    @PostMapping("/edit")
    public String editUser(@RequestParam String name, @RequestParam String email, HttpSession session, Model model) {
        MovieUser loggedInUser = (MovieUser) session.getAttribute("user");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Update user details
        loggedInUser.setName(name);
        loggedInUser.setEmail(email);
        userService.updateUser(loggedInUser.getId(), loggedInUser);

        model.addAttribute("success", "Profile updated successfully");
        return "redirect:/profile";
    }





    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
