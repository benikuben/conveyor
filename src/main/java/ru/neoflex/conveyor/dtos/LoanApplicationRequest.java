package ru.neoflex.conveyor.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.neoflex.conveyor.util.validators.OverEighteenYearsOld;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Schema(description = "Данные пользователя, необходимые для отправки заявки на кредит")
public class LoanApplicationRequest {
    @Schema(description = "Сумма", example = "10000")
    @DecimalMin(value = "10000", message = "Сумма кредита должна быть больше или равна 10 000")
    @Digits(integer = 10, fraction = 2, message = "Значение суммы кредита должно быть в формате 0.00")
    @NotNull(message = "Должна быть введена корректная сумма кредита")
    private BigDecimal amount;

    @Schema(description = "Срок", example = "6")
    @Min(value = 6, message = "Срок кредита должен быть больше или равен 6")
    @NotNull(message = "Должен быть введен корректный срок кредита")
    private Integer term;

    @Schema(description = "Имя", example = "Ivan")
    @Pattern(regexp = "^[A-Za-z]{2,30}$", message = "Имя должно быть длиной от 2 до 30 латинских букв")
    private String firstName;

    @Schema(description = "Фамилия", example = "Ivanov")
    @Pattern(regexp = "^[A-Za-z]{2,30}$", message = "Фамилия должна быть длиной от 2 до 30 латинских букв")
    private String lastName;

    @Schema(description = "Отчество", example = "Ivanovich")
    @Pattern(regexp = "^.{0}|[A-Za-z]{2,30}$", message = "Отчество должно быть длиной от 2 до 30 латинских букв")
    private String middleName;

    @Schema(description = "Email", example = "ivan@mail.ru")
    @Pattern(regexp = "^[\\w.]{2,50}@[\\w.]{2,20}$", message = "Должен быть введен корректный email")
    private String email;

    @Schema(description = "Дата рождения", example = "2000-01-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @OverEighteenYearsOld
    @NotNull(message = "Должна быть указана дата рождения в формате гггг-мм-дд")
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта", example = "1234")
    @Pattern(regexp = "^[0-9]{4}$", message = "Серия паспорта должна содержать 4 цифры")
    private String passportSeries;

    @Schema(description = "Номер паспорта", example = "123456")
    @Pattern(regexp = "^[0-9]{6}$", message = "Номер паспорта должен содержать 6 цифр")
    private String passportNumber;
}
