package cz.uhk.dbsproject.controller;

import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.repository.UserRepository;
import cz.uhk.dbsproject.service.LogService;
import cz.uhk.dbsproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogService logService;

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        MovieUser user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPasswordHash())) {
            session.setAttribute("user", user);
            logService.log(user, "User logged in");
            return "redirect:/movies";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/change-password")
    public String changePasswordForm(HttpSession session, Model model) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword, @RequestParam String confirmNewPassword, HttpSession session, Model model) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            model.addAttribute("error", "Current password is incorrect");
            return "change-password";
        }

        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "change-password";
        }

        if(passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            model.addAttribute("error", "New password cannot be the same as the current password");
            return "change-password";
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logService.log(user, "Password changed successfully");
        model.addAttribute("success", "Password changed successfully");
        return "change-password";
    }


    @GetMapping("/profile/edit")
    public String editProfileForm(HttpSession session, Model model) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String editProfile(@RequestParam String name, @RequestParam String email, HttpSession session, Model model) {
        MovieUser user = (MovieUser) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        // Update user details
        user.setName(name);
        user.setEmail(email);
        userRepository.save(user);
        logService.log(user, "User profile updated");

        model.addAttribute("success", "Profile updated successfully");
        return "redirect:/profile";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email, @RequestParam String password, Model model) {
        if(userService.getUserByEmail(email) != null) {
            model.addAttribute("error", "Email already exists");
            return "register";
        };
        MovieUser user = new MovieUser();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole("USER");
        userRepository.save(user);
        logService.log(user, "User Registered");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
