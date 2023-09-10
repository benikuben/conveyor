package ru.neoflex.conveyor.services;

import ru.neoflex.conveyor.dtos.LoanApplicationRequest;
import ru.neoflex.conveyor.dtos.LoanOffer;

import java.util.List;

public interface OfferService {
    List<LoanOffer> generateOffers(LoanApplicationRequest request);
}
