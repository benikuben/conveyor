package ru.neoflex.conveyor.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.conveyor.dtos.CreditDTO;
import ru.neoflex.conveyor.dtos.LoanApplicationRequestDTO;
import ru.neoflex.conveyor.dtos.LoanOfferDTO;
import ru.neoflex.conveyor.dtos.ScoringDataDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConveyorServiceImpl implements ConveyorService {
    private final OfferService offerService;
    private final CreditService creditService;

    @Override
    public List<LoanOfferDTO> generateOffers(LoanApplicationRequestDTO request) {
        return offerService.generateOffers(request);
    }

    @Override
    public CreditDTO generateCredit(ScoringDataDTO request) {
        return creditService.generateCredit(request);
    }
}
