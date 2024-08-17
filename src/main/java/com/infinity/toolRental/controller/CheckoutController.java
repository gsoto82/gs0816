package com.infinity.toolRental.controller;


import com.infinity.toolRental.model.RentalAgreement;
import com.infinity.toolRental.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/rental")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<RentalAgreement> checkout(
            @RequestParam String toolCode,
            @RequestParam int rentalDays,
            @RequestParam int discountPercent,
            @RequestParam String checkoutDate) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        Date date = sdf.parse(checkoutDate);
        RentalAgreement agreement = checkoutService.checkout(toolCode, rentalDays, discountPercent, date);

        agreement.printAgreement();

        return ResponseEntity.ok(agreement);
    }
}
