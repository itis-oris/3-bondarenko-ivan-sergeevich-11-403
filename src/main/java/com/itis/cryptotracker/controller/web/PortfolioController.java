package com.itis.cryptotracker.controller.web;

import com.itis.cryptotracker.dto.form.PortfolioEntryForm;
import com.itis.cryptotracker.security.UserPrincipal;
import com.itis.cryptotracker.service.CoinService;
import com.itis.cryptotracker.service.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final CoinService coinService;

    @GetMapping
    public String list(@AuthenticationPrincipal UserPrincipal principal, Model model) {
        var entries = portfolioService.findForUser(principal.getId());
        model.addAttribute("entries", entries);
        model.addAttribute("coins", coinService.findAll());
        if (!model.containsAttribute("entryForm")) {
            model.addAttribute("entryForm", new PortfolioEntryForm());
        }
        return "portfolio/list";
    }

    @PostMapping
    public String add(@AuthenticationPrincipal UserPrincipal principal,
                      @Valid @ModelAttribute("entryForm") PortfolioEntryForm form,
                      BindingResult bindingResult,
                      Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("entries", portfolioService.findForUser(principal.getId()));
            model.addAttribute("coins", coinService.findAll());
            return "portfolio/list";
        }
        portfolioService.add(principal.getId(), form);
        return "redirect:/portfolio";
    }

    @PostMapping("/{id}/delete")
    public String delete(@AuthenticationPrincipal UserPrincipal principal, @PathVariable Long id) {
        portfolioService.delete(id, principal.getId());
        return "redirect:/portfolio";
    }
}
