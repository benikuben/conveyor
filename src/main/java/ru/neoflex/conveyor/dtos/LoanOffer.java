package ru.neoflex.conveyor.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
@Schema(description = "Возможные условия кредита")
public class LoanOffer {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long applicationId;

    @Schema(description = "Запрошенная сумма кредита", example = "10000")
    private BigDecimal requestedAmount;

    @Schema(description = "Общая сумма кредита", example = "10150")
    private BigDecimal totalAmount;

    @Schema(description = "Срок кредита", example = "6")
    private Integer term;

    @Schema(description = "Ежемесячный платеж", example = "1756.38")
    private BigDecimal monthlyPayment;

    @Schema(description = "Ставка", example = "13")
    private BigDecimal rate;

    @Schema(description = "Включение страховки", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Статус зарплатного клиента", example = "true")
    private Boolean isSalaryClient;
}
