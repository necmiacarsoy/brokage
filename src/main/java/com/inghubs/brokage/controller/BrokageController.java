package com.inghubs.brokage.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inghubs.brokage.entity.BrokageAsset;
import com.inghubs.brokage.entity.BrokageCustomer;
import com.inghubs.brokage.entity.BrokageOrder;
import com.inghubs.brokage.service.BrokageService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
public class BrokageController {

    private final BrokageService brokageService;

    public BrokageController(BrokageService brokageService) {
        this.brokageService = brokageService;
    }

    @PostMapping("/v1/customers")
    BrokageCustomer createCustomer(@RequestBody @Valid BrokageCustomer customer, Authentication authentication) {
        return brokageService.createCustomer(customer, (BrokageCustomer) authentication.getPrincipal());
    }

    @PostMapping("/v1/orders")
    BrokageOrder createOrder(@RequestBody @Valid BrokageOrder order, Authentication authentication) {
        return brokageService.createOrder(order, (BrokageCustomer) authentication.getPrincipal());
    }

    @GetMapping("/v1/orders")
    List<BrokageOrder> listOrders(@RequestParam @NotBlank String startDate, 
        @RequestParam @NotBlank String endDate,
        Authentication authentication) {
        return brokageService.listOrders(LocalDate.parse(startDate), LocalDate.parse(endDate), 
            (BrokageCustomer) authentication.getPrincipal());
    }

    @DeleteMapping("/v1/orders/{id}")
    void deleteOrder(@PathVariable long id, Authentication authentication) {
        brokageService.deleteOrder(id, (BrokageCustomer) authentication.getPrincipal());
    }

    @GetMapping("/v1/assets")
    List<BrokageAsset> listAssets(Authentication authentication) {
        return brokageService.listAssets((BrokageCustomer) authentication.getPrincipal());
    }
}
