package ru.neoflex.conveyor.services;

import ru.neoflex.conveyor.dtos.PaymentScheduleElement;
import ru.neoflex.conveyor.dtos.ScoringData;

import java.math.BigDecimal;
import java.util.List;

public interface ScoringService {
    void performScoring(ScoringData data);

    BigDecimal calculateRate(Boolean isInsuranceEnabled, Boolean isSalaryClient);

    BigDecimal calculateRate(ScoringData data);

    BigDecimal calculateTotalAmount(BigDecimal amount, Boolean isInsuranceEnabled);

    BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, Integer term, BigDecimal rate);

    List<PaymentScheduleElement> scheduleMonthlyPayments(Integer term, BigDecimal amount, BigDecimal monthlyPayment, BigDecimal rate);

    BigDecimal calculatePsk(BigDecimal monthlyPayment, BigDecimal amount, Integer term);
}
