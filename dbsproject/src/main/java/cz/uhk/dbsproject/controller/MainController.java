package cz.uhk.dbsproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        model.addAttribute("user", session.getAttribute("user"));
        return "index";
    }

    @GetMapping({"/home", "/dashboard"})
    public String redirectToIndex() {
        return "redirect:/";
    }
}