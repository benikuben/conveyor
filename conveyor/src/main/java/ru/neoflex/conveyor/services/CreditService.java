package ru.neoflex.conveyor.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.conveyor.dtos.CreditDTO;
import ru.neoflex.conveyor.dtos.ScoringDataDTO;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final ScoringService scoringService;

    /**
     * Происходит скоринг данных, высчитывание ставки(rate), полной стоимости кредита(psk),
     * размер ежемесячного платежа(monthlyPayment), график ежемесячных платежей (List<PaymentScheduleElement>)
     */
    public CreditDTO generateCredit(ScoringDataDTO data) {
        scoringService.performScoring(data);

        BigDecimal totalAmount = scoringService.calculateTotalAmount(data.getAmount(), data.getIsInsuranceEnabled());
        BigDecimal rate = scoringService.calculateRate(data);
        Integer term = data.getTerm();
        BigDecimal monthlyPayment = scoringService.calculateMonthlyPayment(totalAmount, term, rate);

        return CreditDTO.builder().
                amount(totalAmount).
                term(term).
                monthlyPayment(monthlyPayment).
                rate(rate).
                psk(scoringService.calculatePsk(monthlyPayment, data.getAmount(), term)).
                isInsuranceEnabled(data.getIsInsuranceEnabled()).
                isSalaryClient(data.getIsSalaryClient()).
                paymentSchedule(scoringService.scheduleMonthlyPayments(term, totalAmount, monthlyPayment, rate)).build();
    }
}
