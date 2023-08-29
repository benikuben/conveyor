package ru.neoflex.conveyor.credit_conveyor.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.conveyor.dtos.LoanApplicationRequest;
import ru.neoflex.conveyor.dtos.LoanOffer;
import ru.neoflex.conveyor.services.OfferService;
import ru.neoflex.conveyor.services.ScoringService;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {
    @Mock
    private ScoringService scoringService;

    @InjectMocks
    private OfferService offerService;

    @Test
    void generateOffers() {
        BigDecimal amount = BigDecimal.valueOf(10000);
        Integer term = 6;
        LoanApplicationRequest request = new LoanApplicationRequest();
        request.setAmount(amount);
        request.setTerm(term);

        BigDecimal amountWithInsurance = BigDecimal.valueOf(10150);
        when(scoringService.calculateTotalAmount(any(), any())).
                thenReturn(amount, amount, amountWithInsurance, amountWithInsurance);

        BigDecimal rate = BigDecimal.valueOf(17);
        BigDecimal rateOfSalaryClient = BigDecimal.valueOf(16);
        BigDecimal rateWithIns = BigDecimal.valueOf(14);
        BigDecimal rateOfSalaryClientWithIns = BigDecimal.valueOf(13);
        when(scoringService.calculateRate(any(), any())).
                thenReturn(rate, rateOfSalaryClient, rateWithIns, rateOfSalaryClientWithIns);

        BigDecimal monthlyPayment = BigDecimal.valueOf(1750.27);
        BigDecimal monthlyPaymentOfSalaryClient = BigDecimal.valueOf(1745.30);
        BigDecimal monthlyPaymentWithIns = BigDecimal.valueOf(1761.41);
        BigDecimal monthlyPaymentOfSalaryClientWithIns = BigDecimal.valueOf(1756.38);
        when(scoringService.calculateMonthlyPayment(any(), any(), any())).
                thenReturn(monthlyPayment, monthlyPaymentOfSalaryClient, monthlyPaymentWithIns, monthlyPaymentOfSalaryClientWithIns);

        List<LoanOffer> actualLoanOfferDTOS = offerService.generateOffers(request);

        //tests

        assertNotNull(actualLoanOfferDTOS);

        assertEquals(4, actualLoanOfferDTOS.size());

        verify(scoringService, times(4)).calculateTotalAmount(any(), any());
        verify(scoringService, times(4)).calculateRate(any(), any());
        verify(scoringService, times(4)).calculateMonthlyPayment(any(), any(), any());

        //requestedAmount
        assertEquals(amount, actualLoanOfferDTOS.get(0).getRequestedAmount());

        //totalAmount
        assertEquals(amount, actualLoanOfferDTOS.get(0).getTotalAmount());
        assertEquals(amount, actualLoanOfferDTOS.get(1).getTotalAmount());
        assertEquals(amountWithInsurance, actualLoanOfferDTOS.get(2).getTotalAmount());
        assertEquals(amountWithInsurance, actualLoanOfferDTOS.get(3).getTotalAmount());

        //term
        assertEquals(term, actualLoanOfferDTOS.get(0).getTerm());

        //monthlyPayment
        assertEquals(monthlyPayment, actualLoanOfferDTOS.get(0).getMonthlyPayment());
        assertEquals(monthlyPaymentOfSalaryClient, actualLoanOfferDTOS.get(1).getMonthlyPayment());
        assertEquals(monthlyPaymentWithIns, actualLoanOfferDTOS.get(2).getMonthlyPayment());
        assertEquals(monthlyPaymentOfSalaryClientWithIns, actualLoanOfferDTOS.get(3).getMonthlyPayment());

        //rate
        assertEquals(rate, actualLoanOfferDTOS.get(0).getRate());
        assertEquals(rateOfSalaryClient, actualLoanOfferDTOS.get(1).getRate());
        assertEquals(rateWithIns, actualLoanOfferDTOS.get(2).getRate());
        assertEquals(rateOfSalaryClientWithIns, actualLoanOfferDTOS.get(3).getRate());

        //isInsuranceEnabled
        assertEquals(false, actualLoanOfferDTOS.get(0).getIsInsuranceEnabled());
        assertEquals(false, actualLoanOfferDTOS.get(1).getIsInsuranceEnabled());
        assertEquals(true, actualLoanOfferDTOS.get(2).getIsInsuranceEnabled());
        assertEquals(true, actualLoanOfferDTOS.get(3).getIsInsuranceEnabled());

        //isSalaryClient
        assertEquals(false, actualLoanOfferDTOS.get(0).getIsSalaryClient());
        assertEquals(true, actualLoanOfferDTOS.get(1).getIsSalaryClient());
        assertEquals(false, actualLoanOfferDTOS.get(2).getIsSalaryClient());
        assertEquals(true, actualLoanOfferDTOS.get(3).getIsSalaryClient());
    }
}