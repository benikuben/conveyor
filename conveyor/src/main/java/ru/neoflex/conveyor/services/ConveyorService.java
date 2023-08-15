package ru.neoflex.conveyor.services;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.neoflex.conveyor.dtos.CreditDTO;
import ru.neoflex.conveyor.dtos.LoanApplicationRequestDTO;
import ru.neoflex.conveyor.dtos.LoanOfferDTO;
import ru.neoflex.conveyor.dtos.ScoringDataDTO;

import java.util.List;

public interface ConveyorService {
    List<LoanOfferDTO> generateOffers(LoanApplicationRequestDTO request);
    CreditDTO generateCredit(@RequestBody @Valid ScoringDataDTO request);
}
