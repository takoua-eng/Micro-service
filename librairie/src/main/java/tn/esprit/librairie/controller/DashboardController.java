package tn.esprit.librairie.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.librairie.entity.DTO.DashboardResponse;
import tn.esprit.librairie.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {


    private final DashboardService service;

    @GetMapping
    public DashboardResponse getDashboard() {
        return service.getDashboard();
    }
}
