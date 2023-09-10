package ru.neoflex.conveyor.services;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neoflex.conveyor.dtos.Credit;
import ru.neoflex.conveyor.dtos.LoanApplicationRequest;
import ru.neoflex.conveyor.dtos.LoanOffer;
import ru.neoflex.conveyor.dtos.ScoringData;

import java.util.List;

public interface ConveyorService {
    List<LoanOffer> generateOffers(LoanApplicationRequest request);
    Credit generateCredit(@RequestBody @Valid ScoringData request);
}
