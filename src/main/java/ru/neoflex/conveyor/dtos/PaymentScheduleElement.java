package ru.neoflex.conveyor.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Schema(description = "Ежемесячный платеж")
public class PaymentScheduleElement {
    @Schema(description = "Номер платежа", example = "0")
    private Integer number;

    @Schema(description = "Дата платежа", example = "2023-07-31")
    private LocalDate date;

    @Schema(description = "Сумма платежа", example = "-10000")
    private BigDecimal totalPayment;

    @Schema(description = "Сумма выплаты процентов", example = "0")
    private BigDecimal interestPayment;

    @Schema(description = "Сумма выплаты основного долга", example = "0")
    private BigDecimal debtPayment;

    @Schema(description = "Остаток долга на конец месяца", example = "10000")
    private BigDecimal remainingDebt;

}
