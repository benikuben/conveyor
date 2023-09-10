package ru.neoflex.conveyor.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Schema(description = "Ошибка")
public class ErrorResponse {
    @Schema(description = "Сообщение", example = "Должна быть введена корректная сумма кредита")
    private String message;
}
