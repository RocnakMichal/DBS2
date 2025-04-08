package cz.uhk.dbsproject.controller;

import cz.uhk.dbsproject.entity.MovieUser;
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
}