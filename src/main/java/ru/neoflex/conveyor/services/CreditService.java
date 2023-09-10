package ru.neoflex.conveyor.services;

import ru.neoflex.conveyor.dtos.Credit;
import ru.neoflex.conveyor.dtos.ScoringData;

public interface CreditService {
    Credit generateCredit(ScoringData data);
}
