package ru.neoflex.conveyor.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.conveyor.dtos.LoanApplicationRequest;
import ru.neoflex.conveyor.dtos.LoanOffer;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final ScoringService scoringService;

    /**
     * На основании LoanApplicationRequestDTO создаётся 4 кредитных предложения LoanOfferDTO
     * на основании всех возможных комбинаций булевских полей isInsuranceEnabled и isSalaryClient
     */
    @Override
    public List<LoanOffer> generateOffers(LoanApplicationRequest request) {
        return List.of(
                createOffer(false, false, request),
                createOffer(false, true, request),
                createOffer(true, false, request),
                createOffer(true, true, request)
        );
    }

    private LoanOffer createOffer(Boolean isInsuranceEnabled,
                                  Boolean isSalaryClient,
                                  LoanApplicationRequest request) {
        BigDecimal totalAmount = scoringService.calculateTotalAmount(request.getAmount(), isInsuranceEnabled);
        BigDecimal rate = scoringService.calculateRate(isInsuranceEnabled, isSalaryClient);

        return LoanOffer.builder().
                requestedAmount(request.getAmount()).
                term(request.getTerm()).
                isInsuranceEnabled(isInsuranceEnabled).
                isSalaryClient(isSalaryClient).
                totalAmount(totalAmount).
                rate(rate).
                monthlyPayment(scoringService.calculateMonthlyPayment(totalAmount, request.getTerm(), rate)).build();
    }
}
