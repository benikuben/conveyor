package ru.neoflex.conveyor.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.neoflex.conveyor.util.Gender;
import ru.neoflex.conveyor.util.MaritalStatus;
import ru.neoflex.conveyor.util.validators.OverEighteenYearsOld;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Schema(description = "Данные пользователя, необходимые для полного расчета параметров кредита")
public class ScoringDataDTO {
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
    @Pattern(regexp = "^[A-Za-z]{2,30}$", message = "Имя должно длиной от 2 до 30 латинских букв")
    private String firstName;

    @Schema(description = "Фамилия", example = "Ivanov")
    @Pattern(regexp = "^[A-Za-z]{2,30}$", message = "Фамилия должна быть длиной от 2 до 30 латинских букв")
    private String lastName;

    @Schema(description = "Отчество", example = "Ivanovich")
    @Pattern(regexp = "^.{0}|[A-Za-z]{2,30}$", message = "Отчество должно быть длиной от 2 до 30 латинских букв")
    private String middleName;

    @Schema(description = "Гендер", example = "male")
    @NotNull(message = "Гендер должен быть указан корректно (MALE, FEMALE или NON_BINARY)")
    private Gender gender;

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

    @Schema(description = "Дата выдачи паспорта", example = "2014-01-01")
    @Past(message = "Дата должна быть раньше сгодняшней даты")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Должна быть указана дата выдачи паспорта в формате гггг-мм-дд")
    private LocalDate passportIssueDate;

    @Schema(description = "Кем выдан паспорт", example = "UFMS Moscow")
    @Pattern(regexp = "^[A-Za-z ]{2,50}$", message = "Наименование организации должно быть длиной от 2 до 50 латинских букв")
    private String passportIssueBranch;

    @Schema(description = "Семейное положение", example = "married")
    @NotNull(message = "Семейное положение должно быть указано корректно (MARRIED, DIVORCED, SINGLE или WIDOW_WIDOWER)")
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "1")
    @Min(value = 0, message = "Количество иждивенцев должно быть больше 0")
    @Max(value = 20, message = "Количество иждивенцев должно быть не больше 20")
    @NotNull(message = "Должно быть введено корректное количество иждивенцев")
    private Integer dependentAmount;

    @NotNull(message = "Данные о трудоустройстве должны быть указаны")
    @Valid
    private EmploymentDTO employment;

    @Schema(description = "Номер банковского счета", example = "01234567890123456789")
    @Pattern(regexp = "^\\d{20}$", message = "Номер банковского счета должен содержать 20 цифр")
    private String account;

    @Schema(description = "Включение страховки", example = "false")
    @NotNull(message = "Необходимо указать, включена ли страховка")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Статус зарплатного клиента", example = "false")
    @NotNull(message = "Необходимо указать, являетесь ли Вы зарплатным клиентом")
    private Boolean isSalaryClient;
}
