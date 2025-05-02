package com.example.demo.controller;

import com.example.demo.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/reviews/{id}/approve")
    public void approveReview(@PathVariable Long id) {
        adminService.approveReview(id);
    }

    @PostMapping("/reviews/{id}/reject")
    public void rejectReview(@PathVariable Long id) {
        adminService.rejectReview(id);
    }

    @PostMapping("/portfolio/{id}/approve")
    public void approvePortfolio(@PathVariable Long id) {
        adminService.approvePortfolio(id);
    }

    @PostMapping("/portfolio/{id}/reject")
    public void rejectPortfolio(@PathVariable Long id) {
        adminService.rejectPortfolio(id);
    }

    @PostMapping("/usluga/{id}/approve")
    public void approveUsluga(@PathVariable Long id) {
        adminService.approveUsluga(id);
    }

    @PostMapping("/usluga/{id}/reject")
    public void rejectUsluga(@PathVariable Long id) {
        adminService.rejectUsluga(id);
    }
}
