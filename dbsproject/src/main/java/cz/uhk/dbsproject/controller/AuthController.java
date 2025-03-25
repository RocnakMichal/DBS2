package cz.uhk.dbsproject.controller;

import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        MovieUser user = userRepository.findByEmail(email);
        if (user != null && user.getPasswordHash().equals(password)) {
            session.setAttribute("user", user);
            return "redirect:/users";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email, @RequestParam String password) {
        MovieUser user = new MovieUser();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(password);
        user.setRole("USER");
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
