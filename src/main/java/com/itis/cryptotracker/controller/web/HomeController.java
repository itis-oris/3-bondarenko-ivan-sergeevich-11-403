package com.itis.cryptotracker.controller.web;

import com.itis.cryptotracker.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal != null) {
            return "redirect:/dashboard";
        }
        return "home";
    }
}
