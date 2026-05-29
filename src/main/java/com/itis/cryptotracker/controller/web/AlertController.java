package com.itis.cryptotracker.controller.web;

import com.itis.cryptotracker.mapper.AlertMapper;
import com.itis.cryptotracker.model.Alert;
import com.itis.cryptotracker.model.enums.AlertDirection;
import com.itis.cryptotracker.model.enums.AlertStatus;
import com.itis.cryptotracker.dto.form.AlertForm;
import com.itis.cryptotracker.security.UserPrincipal;
import com.itis.cryptotracker.service.AlertService;
import com.itis.cryptotracker.service.CoinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;
    private final CoinService coinService;
    private final AlertMapper alertMapper;

    @GetMapping
    public String list(@AuthenticationPrincipal UserPrincipal principal,
                       @RequestParam(required = false) AlertStatus status,
                       @RequestParam(required = false) AlertDirection direction,
                       @RequestParam(required = false) String q,
                       Model model) {
        var alerts = alertService.searchUserAlerts(principal.getId(), status, direction, q);
        model.addAttribute("alerts", alerts.stream().map(alertMapper::toResponse).toList());
        model.addAttribute("statusFilter", status);
        model.addAttribute("directionFilter", direction);
        model.addAttribute("query", q);
        return "alerts/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        if (!model.containsAttribute("alertForm")) {
            model.addAttribute("alertForm", new AlertForm());
        }
        model.addAttribute("coins", coinService.findAll());
        model.addAttribute("editing", false);
        return "alerts/form";
    }

    @PostMapping
    public String create(@AuthenticationPrincipal UserPrincipal principal,
                         @Valid @ModelAttribute("alertForm") AlertForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("coins", coinService.findAll());
            model.addAttribute("editing", false);
            return "alerts/form";
        }
        alertService.createFromForm(principal.getId(), form);
        return "redirect:/alerts";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@AuthenticationPrincipal UserPrincipal principal,
                           @PathVariable Long id,
                           Model model) {
        Alert alert = alertService.findOwned(id, principal.getId());
        AlertForm form = new AlertForm();
        form.setId(alert.getId());
        form.setCoinId(alert.getCoin().getId());
        form.setDirection(alert.getDirection());
        form.setTargetPrice(alert.getTargetPrice());
        form.setChannel(alert.getChannel());
        form.setComment(alert.getComment());
        model.addAttribute("alertForm", form);
        model.addAttribute("coins", coinService.findAll());
        model.addAttribute("editing", true);
        return "alerts/form";
    }

    @PostMapping("/{id}")
    public String update(@AuthenticationPrincipal UserPrincipal principal,
                         @PathVariable Long id,
                         @Valid @ModelAttribute("alertForm") AlertForm form,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("coins", coinService.findAll());
            model.addAttribute("editing", true);
            return "alerts/form";
        }
        alertService.update(id, principal.getId(), form);
        return "redirect:/alerts";
    }

    @PostMapping("/{id}/delete")
    public String delete(@AuthenticationPrincipal UserPrincipal principal,
                         @PathVariable Long id) {
        alertService.delete(id, principal.getId());
        return "redirect:/alerts";
    }

    @PostMapping("/{id}/reactivate")
    public String reactivate(@AuthenticationPrincipal UserPrincipal principal,
                             @PathVariable Long id) {
        alertService.reactivate(id, principal.getId());
        return "redirect:/alerts";
    }

    @PostMapping("/{id}/disable")
    public String disable(@AuthenticationPrincipal UserPrincipal principal,
                          @PathVariable Long id) {
        alertService.disable(id, principal.getId());
        return "redirect:/alerts";
    }
}
