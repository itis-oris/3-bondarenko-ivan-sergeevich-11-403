package com.itis.cryptotracker.controller.web;

import com.itis.cryptotracker.dto.form.RegisterForm;
import com.itis.cryptotracker.exception.ServiceException;
import com.itis.cryptotracker.security.UserPrincipal;
import com.itis.cryptotracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal UserPrincipal principal,
                            @RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "registered", required = false) String registered,
                            Model model) {
        if (principal != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("hasError", error != null);
        model.addAttribute("hasLogout", logout != null);
        model.addAttribute("hasRegistered", registered != null);
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        if (principal != null) {
            return "redirect:/dashboard";
        }
        if (!model.containsAttribute("registerForm")) {
            model.addAttribute("registerForm", new RegisterForm());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerForm") RegisterForm form,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        try {
            userService.register(form);
        } catch (ServiceException ex) {
            bindingResult.addError(new FieldError("registerForm", "username", ex.getMessage()));
            return "auth/register";
        }
        return "redirect:/login?registered";
    }
}
