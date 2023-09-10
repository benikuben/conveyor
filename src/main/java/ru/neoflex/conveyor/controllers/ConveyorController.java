package ru.neoflex.conveyor.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.conveyor.dtos.Credit;
import ru.neoflex.conveyor.services.ConveyorService;
import ru.neoflex.conveyor.dtos.LoanApplicationRequest;
import ru.neoflex.conveyor.dtos.LoanOffer;
import ru.neoflex.conveyor.dtos.ScoringData;

import java.util.List;

@RestController
@RequestMapping("/conveyor")
@RequiredArgsConstructor
@Tag(name = "Conveyor Controller", description = "Позволяет произвести расчёт условий и параметров кредита")
public class ConveyorController {
    private final ConveyorService conveyorService;

    /**
     * POST: /conveyor/offers - расчёт возможных условий кредита.
     * Request - LoanApplicationRequestDTO, response - List<LoanOfferDTO>
     */
    @Operation(
            summary = "Расчёт возможных условий кредита",
            description = "Позволяет расчитать возможные условия кредита"
    )
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOffer>> generateOffers(@RequestBody @Valid LoanApplicationRequest request) {
        return ResponseEntity.ok(conveyorService.generateOffers(request));
    }

    /**
     * POST: /conveyor/calculation - валидация присланных данных + скоринг данных + полный расчет параметров кредита.
     * Request - ScoringDataDTO, response CreditDTO.
     */
    @Operation(
            summary = "Полный расчет параметров кредита",
            description = "Позволяет произвести валидацию и скоринг присланных данных, полный расчет параметров кредита"
    )
    @PostMapping("/calculation")
    public ResponseEntity<Credit> generateCredit(@RequestBody @Valid ScoringData request) {
        return ResponseEntity.ok(conveyorService.generateCredit(request));
    }
}
