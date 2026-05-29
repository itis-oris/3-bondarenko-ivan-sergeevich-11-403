package com.itis.cryptotracker.controller.web;

import com.itis.cryptotracker.service.AdminCoinService;
import com.itis.cryptotracker.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/coins")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminCoinController {

    private final CoinService coinService;
    private final AdminCoinService adminCoinService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("coins", coinService.findAll());
        model.addAttribute("tags", adminCoinService.findAllTags());
        return "admin/coins";
    }

    @PostMapping
    public String add(@RequestParam String coinGeckoId,
                      @RequestParam(required = false) List<Long> tagIds) {
        adminCoinService.addCoin(coinGeckoId, tagIds);
        return "redirect:/admin/coins";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        adminCoinService.deleteCoin(id);
        return "redirect:/admin/coins";
    }

    @PostMapping("/tags")
    public String addTag(@RequestParam String name) {
        adminCoinService.addTag(name);
        return "redirect:/admin/coins";
    }
}
