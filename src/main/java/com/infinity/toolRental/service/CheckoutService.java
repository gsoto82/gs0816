package com.infinity.toolRental.service;


import com.infinity.toolRental.model.RentalAgreement;
import com.infinity.toolRental.model.Tool;
import com.infinity.toolRental.repository.RentalAgreementRepository;
import com.infinity.toolRental.repository.ToolRepository;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class CheckoutService {

    private final ToolRepository toolRepository;
    private final RentalAgreementRepository rentalAgreementRepository;

    public CheckoutService(ToolRepository toolRepository, RentalAgreementRepository rentalAgreementRepository) {
        this.toolRepository = toolRepository;
        this.rentalAgreementRepository = rentalAgreementRepository;
    }

    public RentalAgreement checkout(String toolCode, int rentalDays, int discountPercent, Date checkoutDate) {
        validateCheckoutParams(rentalDays, discountPercent);

        Tool tool = toolRepository.findById(toolCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tool code."));
        Date dueDate = calculateDueDate(checkoutDate, rentalDays);
        int chargeDays = calculateChargeDays(tool, checkoutDate, rentalDays);
        double preDiscountCharge = chargeDays * tool.getDailyCharge();
        double discountAmount = (discountPercent / 100.0) * preDiscountCharge;
        double finalCharge = preDiscountCharge - discountAmount;

        RentalAgreement rentalAgreement = new RentalAgreement(tool.getToolCode(), tool.getToolType(), tool.getBrand(),
                rentalDays, checkoutDate, dueDate, tool.getDailyCharge(), chargeDays, preDiscountCharge, discountPercent,
                discountAmount, finalCharge);

        return rentalAgreementRepository.save(rentalAgreement);
    }

    private void validateCheckoutParams(int rentalDays, int discountPercent) {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }
    }

    private Date calculateDueDate(Date checkoutDate, int rentalDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkoutDate);
        calendar.add(Calendar.DAY_OF_YEAR, rentalDays);
        return calendar.getTime();
    }

    private int calculateChargeDays(Tool tool, Date checkoutDate, int rentalDays) {
        int chargeDays = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkoutDate);

        for (int i = 0; i < rentalDays; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            boolean isWeekend = (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
            boolean isHoliday = isHoliday(calendar);


            if (tool.isWeekdayCharge() && !isWeekend && !isHoliday) {
                chargeDays++;
            } else if (tool.isWeekendCharge() && isWeekend && !isHoliday) {
                chargeDays++;
            } else if (tool.isHolidayCharge() && isHoliday) {
                if (tool.isWeekendCharge() && isWeekend) {
                    rentalDays++;
                }
            }
    }
        return chargeDays;
    }

    private boolean isHoliday(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (month == Calendar.JULY && day == 4) {
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek == Calendar.SATURDAY) {
                return true; // Observed on Friday
            } else if (dayOfWeek == Calendar.SUNDAY) {
                return true; // Observed on Monday
            }
            return true; // Exact date is a weekday
        }


        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);

        // Check for Labor Day (First Monday in September)
        if (month == Calendar.SEPTEMBER && dayOfWeek == Calendar.MONDAY) {
            Calendar firstDayOfMonth = (Calendar) calendar.clone();
            firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);

            int firstDayOfWeek = firstDayOfMonth.get(Calendar.DAY_OF_WEEK);

            // Calculate the date of the first Monday in September
            int firstMonday = (firstDayOfWeek == Calendar.MONDAY) ? 1 : 9 - firstDayOfWeek;
            if (day == firstMonday || (day > firstMonday && dayOfWeek == Calendar.MONDAY)) {
                return true; // It's Labor Day
            }
        }
        return false;
    }
}
