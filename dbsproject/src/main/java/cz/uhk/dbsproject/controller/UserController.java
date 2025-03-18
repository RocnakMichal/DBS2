package cz.uhk.dbsproject.controller;

import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<MovieUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public MovieUser getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @PostMapping
    public MovieUser createUser(@RequestBody MovieUser movieUser) {
        return userService.createUser(movieUser);
    }

    @PutMapping("/{id}")
    public MovieUser updateUser(@PathVariable int id, @RequestBody MovieUser updatedMovieUser) {
        return userService.updateUser(id, updatedMovieUser);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return "User with ID " + id + " deleted.";
        } else {
            return "User with ID " + id + " not found.";
        }
    }
}
