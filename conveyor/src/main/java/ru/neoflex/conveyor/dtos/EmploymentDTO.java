package ru.neoflex.conveyor.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Schema(description = "Данные о трудоустройстве пользователя")
public class EmploymentDTO {
    @Schema(description = "Рабочий статус", example = "business owner")
    @NotNull(message = "Рабочий статус должен быть указан корректно (UNEMPLOYED, SELF_EMPLOYED, EMPLOYED или BUSINESS_OWNER)")
    private EmploymentStatus employmentStatus;

    @Schema(description = "ИНН работодателя", example = "0123456789")
    @Pattern(regexp = "^\\d{10}$", message = "ИНН работодателя должен содержать 10 цифр")
    private String employerINN;

    @Schema(description = "Зарплата", example = "10000")
    @Digits(integer = 10, fraction = 2, message = "Значение зарплаты должно быть в формате 0.00")
    @NotNull(message = "Зарплата должна быть введена корректна")
    private BigDecimal salary;

    @Schema(description = "Позиция на работе", example = "middle manager")
    @NotNull(message = "Позиция на работе должна быть указана корректно (WORKER, MID_MANAGER, TOP_MANAGER или OWNER)")
    private Position position;

    @Schema(description = "Общий стаж работы (в месяцах)", example = "12")
    @Min(value = 0, message = "Общий стаж работы (в месяцах) должен быть больше или равен 0")
    @NotNull(message = "Должен быть введен корректный общий стаж работы (в месяцах)")
    private Integer workExperienceTotal;

    @Schema(description = "Текущий стаж работы (в месяцах)", example = "3")
    @Min(value = 0, message = "Текущий стаж работы (в месяцах) должен быть больше или равен 0")
    @NotNull(message = "Должен быть введен корректный текущий стаж работы (в месяцах)")
    private Integer workExperienceCurrent;
}
