package com.locomotive.consume_service.controller;

import com.locomotive.consume_service.dto.GetAllDataRes;
import com.locomotive.consume_service.model.LocomotiveSummary;
import com.locomotive.consume_service.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @GetMapping(value = "/getlocomotivelist")
    public GetAllDataRes getLocomotiveList() {
        return customerService.getLocomotiveList();
    }

    @GetMapping(value = "/getdetaillocomotive")
    public LocomotiveSummary getDetailLocomotive(@RequestParam("locomotiveCode") String locomotiveCode) {
        return customerService.getDetailLocomotive(locomotiveCode);
    }
}
