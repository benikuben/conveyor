package ru.neoflex.conveyor.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.conveyor.dtos.Credit;
import ru.neoflex.conveyor.dtos.ScoringData;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final ScoringService scoringService;

    /**
     * Происходит скоринг данных, высчитывание ставки(rate), полной стоимости кредита(psk),
     * размер ежемесячного платежа(monthlyPayment), график ежемесячных платежей (List<PaymentScheduleElement>)
     */
    @Override
    public Credit generateCredit(ScoringData data) {
        scoringService.performScoring(data);

        BigDecimal totalAmount = scoringService.calculateTotalAmount(data.getAmount(), data.getIsInsuranceEnabled());
        BigDecimal rate = scoringService.calculateRate(data);
        Integer term = data.getTerm();
        BigDecimal monthlyPayment = scoringService.calculateMonthlyPayment(totalAmount, term, rate);

        return Credit.builder().
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
