package ru.neoflex.conveyor.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.conveyor.dtos.CreditDTO;
import ru.neoflex.conveyor.services.CreditService;
import ru.neoflex.conveyor.services.OfferService;
import ru.neoflex.conveyor.dtos.LoanApplicationRequestDTO;
import ru.neoflex.conveyor.dtos.LoanOfferDTO;
import ru.neoflex.conveyor.dtos.ScoringDataDTO;

import java.util.List;

@RestController
@RequestMapping("/conveyor")
@RequiredArgsConstructor
@Tag(name = "Conveyor Controller", description = "Позволяет произвести расчёт условий и параметров кредита")
public class ConveyorController {
    private final OfferService offerService;
    private final CreditService creditService;

    /**
     * POST: /conveyor/offers - расчёт возможных условий кредита.
     * Request - LoanApplicationRequestDTO, response - List<LoanOfferDTO>
     */
    @Operation(
            summary = "Расчёт возможных условий кредита",
            description = "Позволяет расчитать возможные условия кредита"
    )
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDTO>> generateOffers(@RequestBody @Valid LoanApplicationRequestDTO request) {
        return ResponseEntity.ok(offerService.generateOffers(request));
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
    public ResponseEntity<CreditDTO> generateCredit(@RequestBody @Valid ScoringDataDTO request) {
        return ResponseEntity.ok(creditService.generateCredit(request));
    }
}
