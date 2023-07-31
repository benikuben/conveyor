package ru.neoflex.conveyor.credit_conveyor.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.conveyor.credit_conveyor.dtos.*;
import ru.neoflex.conveyor.credit_conveyor.services.CreditService;
import ru.neoflex.conveyor.credit_conveyor.services.OfferService;
import ru.neoflex.conveyor.credit_conveyor.util.exceptions.LoanRejectionException;
import ru.neoflex.conveyor.credit_conveyor.util.exceptions.ValidationErrorMessage;
import ru.neoflex.conveyor.credit_conveyor.util.exceptions.ErrorResponse;
import ru.neoflex.conveyor.credit_conveyor.util.exceptions.ValidationException;

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
    public List<LoanOfferDTO> generateOffers(@RequestBody @Valid LoanApplicationRequestDTO request,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(ValidationErrorMessage.createErrorMessage(bindingResult));
        }

        return offerService.generateOffers(request);
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
    public CreditDTO generateCredit(@RequestBody @Valid ScoringDataDTO request,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(ValidationErrorMessage.createErrorMessage(bindingResult));
        }
        return creditService.generateCredit(request);
    }

    /**
     * Обработка исключения. Невалидные данные
     */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(ValidationException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка исключения. Отказ в одобрении кредита
     */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(LoanRejectionException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
