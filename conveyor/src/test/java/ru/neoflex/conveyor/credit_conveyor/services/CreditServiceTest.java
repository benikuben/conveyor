package ru.neoflex.conveyor.credit_conveyor.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.conveyor.dtos.Credit;
import ru.neoflex.conveyor.dtos.PaymentScheduleElement;
import ru.neoflex.conveyor.dtos.ScoringData;
import ru.neoflex.conveyor.services.CreditService;
import ru.neoflex.conveyor.services.ScoringService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {
    @Mock
    private ScoringService scoringService;

    @InjectMocks
    private CreditService creditService;

    @Test
    void generateCredit() {
        Integer term = 6;
        Boolean isInsuranceEnabled = false;
        Boolean isSalaryClient = false;
        ScoringData data = new ScoringData();
        data.setTerm(term);
        data.setIsInsuranceEnabled(isInsuranceEnabled);
        data.setIsSalaryClient(isSalaryClient);

        BigDecimal amount = BigDecimal.valueOf(10000);
        BigDecimal monthlyPayment = BigDecimal.valueOf(1750.27);
        BigDecimal rate = BigDecimal.valueOf(17);
        BigDecimal psk = BigDecimal.valueOf(10.032);
        List<PaymentScheduleElement> payments = List.of(
                new PaymentScheduleElement(0, LocalDate.of(2000, 1, 1), amount.negate(), BigDecimal.ZERO, BigDecimal.ZERO, amount),
                new PaymentScheduleElement(1, LocalDate.of(2000, 2, 1), monthlyPayment, BigDecimal.valueOf(141.67), BigDecimal.valueOf(1608.60), BigDecimal.valueOf(8391.40)),
                new PaymentScheduleElement(2, LocalDate.of(2000, 3, 1), monthlyPayment, BigDecimal.valueOf(118.88), BigDecimal.valueOf(1608.60), BigDecimal.valueOf(8391.40)),
                new PaymentScheduleElement(3, LocalDate.of(2000, 4, 1), monthlyPayment, BigDecimal.valueOf(95.77), BigDecimal.valueOf(1654.50), BigDecimal.valueOf(5105.51)),
                new PaymentScheduleElement(4, LocalDate.of(2000, 5, 1), monthlyPayment, BigDecimal.valueOf(72.33), BigDecimal.valueOf(1677.94), BigDecimal.valueOf(3427.57)),
                new PaymentScheduleElement(5, LocalDate.of(2000, 6, 1), monthlyPayment, BigDecimal.valueOf(48.56), BigDecimal.valueOf(1701.71), BigDecimal.valueOf(1725.86)),
                new PaymentScheduleElement(6, LocalDate.of(2000, 7, 1), monthlyPayment, BigDecimal.valueOf(24.45), BigDecimal.valueOf(1725.82), BigDecimal.valueOf(0.04))
        );
        when(scoringService.calculateTotalAmount(any(), any())).thenReturn(amount);
        when(scoringService.calculateMonthlyPayment(any(), any(), any())).thenReturn(monthlyPayment);
        when(scoringService.calculateRate(any())).thenReturn(rate);
        when(scoringService.calculatePsk(any(), any(), any())).thenReturn(psk);
        when(scoringService.scheduleMonthlyPayments(any(), any(), any(), any())).thenReturn(payments);

        Credit actualCreditDTO = creditService.generateCredit(data);

        //tests

        assertEquals(amount, actualCreditDTO.getAmount());
        assertEquals(term, actualCreditDTO.getTerm());
        assertEquals(monthlyPayment, actualCreditDTO.getMonthlyPayment());
        assertEquals(rate, actualCreditDTO.getRate());
        assertEquals(psk, actualCreditDTO.getPsk());
        assertEquals(isInsuranceEnabled, actualCreditDTO.getIsInsuranceEnabled());
        assertEquals(isSalaryClient, actualCreditDTO.getIsSalaryClient());
        assertEquals(payments, actualCreditDTO.getPaymentSchedule());
    }
}