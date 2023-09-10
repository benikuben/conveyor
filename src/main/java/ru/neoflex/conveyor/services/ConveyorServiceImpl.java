package ru.neoflex.conveyor.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.conveyor.dtos.Credit;
import ru.neoflex.conveyor.dtos.LoanApplicationRequest;
import ru.neoflex.conveyor.dtos.LoanOffer;
import ru.neoflex.conveyor.dtos.ScoringData;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConveyorServiceImpl implements ConveyorService {
    private final OfferService offerService;
    private final CreditService creditService;

    @Override
    public List<LoanOffer> generateOffers(LoanApplicationRequest request) {
        return offerService.generateOffers(request);
    }

    @Override
    public Credit generateCredit(ScoringData request) {
        return creditService.generateCredit(request);
    }
}
