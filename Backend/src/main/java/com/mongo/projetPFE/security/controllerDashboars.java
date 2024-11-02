package com.mongo.projetPFE.security;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class controllerDashboars {
    @GetMapping("/login")
    public String home(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            model.addAttribute("userName", principal.getAttribute("name"));
            return "index";
        }
        return "index";
    }

    @GetMapping("/aa")
    public String home1(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal != null) {
            model.addAttribute("userName", principal.getAttribute("name"));
        }
        return "index";
    }
}


