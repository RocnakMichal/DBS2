package cz.uhk.dbsproject.controller;

import cz.uhk.dbsproject.entity.MovieUser;
import cz.uhk.dbsproject.service.LogService;
import cz.uhk.dbsproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final LogService logService;
    @Autowired
    private UserService userService;

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

    @PostMapping("/promote/{id}")
    public String toggleUserRole(@PathVariable int id, HttpSession session) {
        // Check if current user is admin
        if (!isAdmin(session)) {
            return "redirect:/";
        }

        // Get the user to modify
        MovieUser user = userService.getUser(id);

        // Toggle role between ADMIN and USER
        if (user.getRole().equals("ADMIN")) {
            userService.demoteAdmin(id);
        } else {
            userService.promoteToAdmin(id);
        }

        return "redirect:/users";
    }


    @PostMapping("/demote/{id}")
    public String demoteAdmin(@PathVariable int id) {
        userService.demoteAdmin(id);
        return "redirect:/users";
    }

}