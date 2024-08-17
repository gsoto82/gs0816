package com.infinity.toolRental.service;


import com.infinity.toolRental.model.RentalAgreement;
import com.infinity.toolRental.model.Tool;
import com.infinity.toolRental.repository.RentalAgreementRepository;
import com.infinity.toolRental.repository.ToolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CheckoutServiceTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    private ToolRepository toolRepository;
    private RentalAgreementRepository rentalAgreementRepository;
    private CheckoutService checkoutService;

    @BeforeEach
    public void setUp() {
        toolRepository = Mockito.mock(ToolRepository.class);
        rentalAgreementRepository = Mockito.mock(RentalAgreementRepository.class);
        checkoutService = new CheckoutService(toolRepository, rentalAgreementRepository);

        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void testInvalidRentalDays() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.checkout("JAKR", 0, 10, new Date());
        });
        assertEquals("Rental day count must be 1 or greater.", exception.getMessage());
    }

    @Test
    public void testInvalidDiscountPercent() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.checkout("JAKR", 5, 110, new Date());
        });
        assertEquals("Discount percent must be between 0 and 100.", exception.getMessage());
    }

    @Test
    public void testToolNotFound() {
        when(toolRepository.findById("JAKR")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            checkoutService.checkout("JAKR", 5, 10, new Date());
        });
        assertEquals("Invalid tool code.", exception.getMessage());
    }

    @Test
    public void testValidCheckout() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        Date checkoutDate = sdf.parse("07/02/20");

        Tool tool = new Tool("LADW", "Ladder", "Werner", 1.99, true, true, false);
        when(toolRepository.findById("LADW")).thenReturn(Optional.of(tool));
        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        RentalAgreement agreement = checkoutService.checkout("LADW", 3, 10, checkoutDate);

        assertNotNull(agreement);
        assertEquals("LADW", agreement.getToolCode());
        assertEquals(3, agreement.getRentalDays());
        assertEquals(10, agreement.getDiscountPercent());
        assertEquals(2, agreement.getChargeDays());assertEquals(3.98, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0.40, agreement.getDiscountAmount(), 0.01);
        assertEquals(3.58, agreement.getFinalCharge(), 0.01);

        agreement.printAgreement();
        String expectedOutput = "Tool code: LADW\n" +
                "Tool type: Ladder\n" +
                "Tool brand: Werner\n" +
                "Rental days: 3\n" +
                "Check out date: 07/02/20\n" +
                "Due date: 07/05/20\n" +
                "Daily rental charge: $1.99\n" +
                "Charge days: 2\n" +
                "Pre-discount charge: $3.98\n" +
                "Discount percent: 10%\n" +
                "Discount amount: $0.40\n" +
                "Final charge: $3.58\n";

        assertEquals(expectedOutput.trim(), outputStreamCaptor.toString().trim());
    }

    @Test
    public void testCheckoutWithHoliday() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        Date checkoutDate = sdf.parse("07/02/20");

        Tool tool = new Tool("CHNS", "Chainsaw", "Stihl", 1.49, true, false, true);
        when(toolRepository.findById("CHNS")).thenReturn(Optional.of(tool));
        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        RentalAgreement agreement = checkoutService.checkout("CHNS", 5, 25, checkoutDate);

        assertNotNull(agreement);
        assertEquals("CHNS", agreement.getToolCode());
        assertEquals(5, agreement.getRentalDays());
        assertEquals(25, agreement.getDiscountPercent());
        assertEquals(3, agreement.getChargeDays()); //
        assertEquals(4.47, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(1.12, agreement.getDiscountAmount(), 0.01);
        assertEquals(3.35, agreement.getFinalCharge(), 0.01);

        // Print the agreement to capture its output
        agreement.printAgreement();

        // Expected output
        String expectedOutput = "Tool code: CHNS\n" +
                "Tool type: Chainsaw\n" +
                "Tool brand: Stihl\n" +
                "Rental days: 5\n" +
                "Check out date: 07/02/20\n" +
                "Due date: 07/07/20\n" +
                "Daily rental charge: $1.49\n" +
                "Charge days: 3\n" +
                "Pre-discount charge: $4.47\n" +
                "Discount percent: 25%\n" +
                "Discount amount: $1.12\n" +
                "Final charge: $3.35\n";

        assertEquals(expectedOutput.trim(), outputStreamCaptor.toString().trim());


    }

    @Test
    public void testCheckoutWithWeekend() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        Date checkoutDate = sdf.parse("07/02/20");

        Tool tool = new Tool("JAKR", "Jackhammer", "Ridgid", 2.99, true, false, false);
        when(toolRepository.findById("JAKR")).thenReturn(Optional.of(tool));
        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        RentalAgreement agreement = checkoutService.checkout("JAKR", 4, 50, checkoutDate);

        assertNotNull(agreement);
        assertEquals("JAKR", agreement.getToolCode());
        assertEquals(4, agreement.getRentalDays());
        assertEquals(50, agreement.getDiscountPercent());
        assertEquals(2, agreement.getChargeDays()); // Only 07/03 is a charge day
        assertEquals(5.98, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(2.99, agreement.getDiscountAmount(), 0.01);
        assertEquals(2.99, agreement.getFinalCharge(), 0.01);



        // Print the agreement to capture its output
        agreement.printAgreement();

        // Expected output
        String expectedOutput = "Tool code: JAKR\n" +
                "Tool type: Jackhammer\n" +
                "Tool brand: Ridgid\n" +
                "Rental days: 4\n" +
                "Check out date: 07/02/20\n" +
                "Due date: 07/06/20\n" +
                "Daily rental charge: $2.99\n" +
                "Charge days: 2\n" +
                "Pre-discount charge: $5.98\n" +
                "Discount percent: 50%\n" +
                "Discount amount: $2.99\n" +
                "Final charge: $2.99\n";



        // Assert the captured output matches the expected output
        assertEquals(expectedOutput.trim(), outputStreamCaptor.toString().trim());
    }

    @Test
    public void testCheckoutWithZeroDiscount() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        Date checkoutDate = sdf.parse("09/03/15");

        Tool tool = new Tool("JAKD", "Jackhammer", "DeWalt", 2.99, true, false, false);
        when(toolRepository.findById("JAKD")).thenReturn(Optional.of(tool));
        when(rentalAgreementRepository.save(any(RentalAgreement.class))).thenAnswer(i -> i.getArguments()[0]);

        RentalAgreement agreement = checkoutService.checkout("JAKD", 6, 0, checkoutDate);

        assertNotNull(agreement);
        assertEquals("JAKD", agreement.getToolCode());
        assertEquals(6, agreement.getRentalDays());
        assertEquals(0, agreement.getDiscountPercent());
         assertEquals(3, agreement.getChargeDays());
        assertEquals(8.97, agreement.getPreDiscountCharge(), 0.01);
        assertEquals(0.00, agreement.getDiscountAmount(), 0.01);
        assertEquals(8.97, agreement.getFinalCharge(), 0.01);

        agreement.printAgreement();

        // Expected output
        String expectedOutput = "Tool code: JAKD\n" +
                "Tool type: Jackhammer\n" +
                "Tool brand: DeWalt\n" +
                "Rental days: 6\n" +
                "Check out date: 09/03/15\n" +
                "Due date: 09/09/15\n" +
                "Daily rental charge: $2.99\n" +
                "Charge days: 3\n" +
                "Pre-discount charge: $8.97\n" +
                "Discount percent: 0%\n" +
                "Discount amount: $0.00\n" +
                "Final charge: $8.97\n";

        // Assert the captured output matches the expected output
        assertEquals(expectedOutput.trim(), outputStreamCaptor.toString().trim());
    }
}
