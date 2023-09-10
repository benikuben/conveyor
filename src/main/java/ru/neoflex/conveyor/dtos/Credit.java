package ru.neoflex.conveyor.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@Schema(description = "Параметры кредита")
public class Credit {
    @Schema(description = "Общая сумма кредита", example = "10000")
    private BigDecimal amount;

    @Schema(description = "Срок кредита", example = "6")
    private Integer term;

    @Schema(description = "Ежемесячный платеж", example = "1740.34")
    private BigDecimal monthlyPayment;

    @Schema(description = "Ставка", example = "15")
    private BigDecimal rate;

    @Schema(description = "ПСК", example = "8.841")
    private BigDecimal psk;

    @Schema(description = "Включение страховки", example = "false")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Статус зарплатного клиента", example = "false")
    private Boolean isSalaryClient;

    private List<PaymentScheduleElement> paymentSchedule;
}
