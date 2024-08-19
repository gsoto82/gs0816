package com.infinity.toolRental.controller;


import com.infinity.toolRental.model.RentalAgreement;
import com.infinity.toolRental.service.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/rental")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }


    @Operation(summary = "Checkout a tool rental", description = "This endpoint processes a tool rental based on the provided parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "404", description = "Tool not found")
    })
    @PostMapping("/checkout")
    public ResponseEntity<RentalAgreement> checkout(
            @Parameter(description = "Code of the tool to be rented", example = "CHNS")
            @RequestParam String toolCode,
            @Parameter(description = "Number of days the tool will be rented", example = "5")
            @RequestParam int rentalDays,
            @Parameter(description = "Discount percentage to be applied", example = "10")
            @RequestParam int discountPercent,
            @Parameter(description = "Date when the tool will be checked out", example = "07/02/2020")
            @RequestParam String checkoutDate) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        Date date = sdf.parse(checkoutDate);
        RentalAgreement agreement = checkoutService.checkout(toolCode, rentalDays, discountPercent, date);

        agreement.printAgreement();

        return ResponseEntity.ok(agreement);
    }
}
