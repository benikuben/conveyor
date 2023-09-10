package ru.neoflex.conveyor.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.neoflex.conveyor.dtos.Employment;
import ru.neoflex.conveyor.dtos.PaymentScheduleElement;
import ru.neoflex.conveyor.dtos.ScoringData;
import ru.neoflex.conveyor.dtos.EmploymentStatus;
import ru.neoflex.conveyor.dtos.Gender;
import ru.neoflex.conveyor.dtos.MaritalStatus;
import ru.neoflex.conveyor.dtos.Position;
import ru.neoflex.conveyor.exceptions.LoanRejectionException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ScoringServiceImpl implements ScoringService {
    @Value("${baseLoanRate}")
    private BigDecimal baseLoanRate;
    @Value("${minimumLoanRate}")
    private BigDecimal minimumLoanRate;
    @Value("${insuranceRate}")
    private BigDecimal insuranceRate;
    private BigDecimal currentRate;
    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);
    private static final Integer SCALE = 10;
    public static final BigDecimal PERCENT = new BigDecimal(100);
    private final BigDecimal SELF_EMPLOYED_PERCENT = BigDecimal.valueOf(1);
    private final BigDecimal BUSINESS_OWNER_PERCENT = BigDecimal.valueOf(3);
    private final BigDecimal MIDDLE_MANAGER_PERCENT = BigDecimal.valueOf(2);
    private final BigDecimal TOP_MANAGER_PERCENT = BigDecimal.valueOf(4);
    private final BigDecimal MARRIED_PERCENT = BigDecimal.valueOf(3);
    private final BigDecimal DIVORCED_PERCENT = BigDecimal.valueOf(1);
    private final BigDecimal DEPENDENT_AMOUNT_PERCENT = BigDecimal.valueOf(1);
    private final BigDecimal INSURANCE_PERCENT = BigDecimal.valueOf(3);
    private final BigDecimal SALARY_CLIENT_PERCENT = BigDecimal.valueOf(1);
    private final BigDecimal FEMALE_AGE_BETWEEN_35_AND_60_PERCENT = BigDecimal.valueOf(3);
    private final BigDecimal MALE_AGE_BETWEEN_30_AND_55_PERCENT = BigDecimal.valueOf(3);
    private final BigDecimal NON_BINARY_PERCENT = BigDecimal.valueOf(3);

    /**
     * Скоринг данных
     * Рабочий статус: Безработный → отказ
     * Сумма займа больше, чем 20 зарплат → отказ
     * Возраст менее 20 или более 60 лет → отказ
     * Стаж работы: Общий стаж менее 12 месяцев → отказ
     * Стаж работы: Текущий стаж менее 3 месяцев → отказ
     */
    @Override
    public void performScoring(ScoringData data) {
        BigDecimal totalAmount = calculateTotalAmount(data.getAmount(), data.getIsInsuranceEnabled());

        log.info("Start of {} {} data scoring. Amount - {}, term - {}", data.getFirstName(), data.getLastName(), data.getAmount(), data.getTerm());

        Employment employment = data.getEmployment();
        int age = calculateAgeByBirthDate(data.getBirthdate());

        if (isEmploymentStatusUnemployed(employment.getEmploymentStatus()) ||
                isAmountMoreThanTwentySalaries(totalAmount, employment.getSalary()) ||
                isAgeLessThanTwentyOrMoreThanSixty(age) ||
                isTotalWorkExperienceLessThanTwelve(employment.getWorkExperienceTotal()) ||
                isCurrentWorkExperienceLessThanThree(employment.getWorkExperienceCurrent())) {
            log.info("Loan rejection");

            throw new LoanRejectionException();
        }
    }

    private Boolean isEmploymentStatusUnemployed(EmploymentStatus status) {
        log.info("Checking whether employmentStatus is unemployed");

        return status == EmploymentStatus.UNEMPLOYED;
    }

    private Boolean isAmountMoreThanTwentySalaries(BigDecimal amount, BigDecimal salary) {
        log.info("Checking whether loan amount is more than 20 salaries");

        return amount.compareTo(salary.multiply(BigDecimal.valueOf(20)).setScale(2, RoundingMode.HALF_EVEN)) >= 0;
    }

    private Boolean isAgeLessThanTwentyOrMoreThanSixty(Integer age) {
        log.info("Checking whether age is less than 20 or more than 60 years");

        return age < 20 || age > 60;
    }

    private Boolean isTotalWorkExperienceLessThanTwelve(Integer workExperienceTotal) {
        log.info("Checking whether total work experience less than 12 months");

        return workExperienceTotal < 12;
    }

    private Boolean isCurrentWorkExperienceLessThanThree(Integer workExperienceCurrent) {
        log.info("Checking whether current work experience less than 3 months");

        return workExperienceCurrent < 3;
    }

    private Integer calculateAgeByBirthDate(LocalDate birthdate) {
        return (int) ChronoUnit.YEARS.between(birthdate, LocalDate.now());
    }

    /**
     * Расчет процентной ставки
     * Добавлена страховка -> ставка уменьшается на 3
     * Зарплатный клиент -> ставка уменьшается на 1
     * Рабочий статус: Самозанятый → ставка увеличивается на 1
     * Рабочий статус: Владелец бизнеса → ставка увеличивается на 3
     * Позиция на работе: Менеджер среднего звена → ставка уменьшается на 2
     * Позиция на работе: Топ-менеджер → ставка уменьшается на 4
     * Семейное положение: Замужем/женат → ставка уменьшается на 3
     * Семейное положение: Разведен → ставка увеличивается на 1
     * Количество иждивенцев больше 1 → ставка увеличивается на 1
     * Пол: Женщина, возраст от 35 до 60 лет → ставка уменьшается на 3
     * Пол: Мужчина, возраст от 30 до 55 лет → ставка уменьшается на 3
     * Пол:  Не бинарный → ставка увеличивается на 3
     */
    @Override
    public BigDecimal calculateRate(Boolean isInsuranceEnabled, Boolean isSalaryClient) {
        log.info("Start of rate calculation");

        currentRate = baseLoanRate;
        calculateRateDependingOnInsurance(isInsuranceEnabled);
        log.info("Rate after its calculation depending on insurance {}", currentRate);

        calculateRateDependingOnSalaryClient(isSalaryClient);
        log.info("Rate after its calculation depending on status of salary client {}", currentRate);

        return currentRate;
    }

    @Override
    public BigDecimal calculateRate(ScoringData data) {
        log.info("Start of rate calculation");

        currentRate = baseLoanRate;
        int age = calculateAgeByBirthDate(data.getBirthdate());

        calculateRateDependingOnEmploymentStatus(data.getEmployment().getEmploymentStatus());
        log.info("Rate after its calculation depending on employment status {}", currentRate);

        calculateRateDependingOnInsurance(data.getIsInsuranceEnabled());
        log.info("Rate after its calculation depending on insurance {}", currentRate);

        calculateRateDependingOnSalaryClient(data.getIsSalaryClient());
        log.info("Rate after its calculation depending on status of salary client {}", currentRate);

        calculateRateDependingOnPosition(data.getEmployment().getPosition());
        log.info("Rate after its calculation depending on position {}", currentRate);

        calculateRateDependingOnMaritalStatus(data.getMaritalStatus());
        log.info("Rate after its calculation depending on marital status {}", currentRate);

        calculateRateDependingOnDependentAmount(data.getDependentAmount());
        log.info("Rate after its calculation depending on dependent amount {}", currentRate);

        calculateRateDependingOnGenderAndAge(data.getGender(), age);
        log.info("Rate after its calculation depending on gender and age {}", currentRate);

        return currentRate;
    }

    private Boolean isCurrentRateGreaterThanMinimum(BigDecimal rate) {
        log.info("Ratio of calculated rate - " + rate + " to minimum - " + minimumLoanRate);

        return rate.compareTo(minimumLoanRate) >= 0;
    }

    private void calculateRateDependingOnEmploymentStatus(EmploymentStatus employmentStatus) {
        log.info("Calculation depending on employment status");

        switch (employmentStatus) {
            case SELF_EMPLOYED -> {
                currentRate = currentRate.add(SELF_EMPLOYED_PERCENT);
                log.info("Employment status - self-employed. Rate increased by 1");
            }
            case BUSINESS_OWNER -> {
                log.info("Employment status - business owner. Rate increased by 3");
                currentRate = currentRate.add(BUSINESS_OWNER_PERCENT);
            }
        }
    }

    private void calculateRateDependingOnPosition(Position position) {
        log.info("Calculation depending on position");

        BigDecimal currentRateBeforeChanges = currentRate;
        switch (position) {
            case MID_MANAGER -> {
                currentRate = currentRate.subtract(MIDDLE_MANAGER_PERCENT);
                log.info("Position - middle manager. Rate reduced by 2");
            }
            case TOP_MANAGER -> {
                currentRate = currentRate.subtract(TOP_MANAGER_PERCENT);
                log.info("Position - top manager. Rate reduced by 4");
            }
        }

        currentRate = isCurrentRateGreaterThanMinimum(currentRate) ? currentRate : currentRateBeforeChanges;
    }

    private void calculateRateDependingOnMaritalStatus(MaritalStatus maritalStatus) {
        log.info("Calculation depending on marital status");

        BigDecimal currentRateBeforeChanges = currentRate;
        switch (maritalStatus) {
            case MARRIED -> {
                currentRate = currentRate.subtract(MARRIED_PERCENT);
                log.info("Marital status - married. Rate reduced by 3");
            }
            case DIVORCED -> {
                currentRate = currentRate.add(DIVORCED_PERCENT);
                log.info("Marital status - divorced. Rate increased by 1");
            }
        }

        currentRate = isCurrentRateGreaterThanMinimum(currentRate) ? currentRate : currentRateBeforeChanges;
    }

    private void calculateRateDependingOnDependentAmount(Integer dependentAmount) {
        log.info("Calculation depending on dependent amount");

        if (dependentAmount > 1) {
            currentRate = currentRate.add(DEPENDENT_AMOUNT_PERCENT);
            log.info("Number of dependants more than 1. Rate increased by 1");
        } else {
            log.info("Number of dependants less than 1. Rate not changed");
        }
    }

    private void calculateRateDependingOnInsurance(Boolean isInsuranceEnabled) {
        log.info("Calculation depending on insurance");

        if (isInsuranceEnabled) {
            currentRate = currentRate.subtract(INSURANCE_PERCENT);
            log.info("Insurance is enabled. Rate reduced by 3");
        } else {
            log.info("Insurance is not enabled. Rate not changed");
        }
    }

    private void calculateRateDependingOnSalaryClient(Boolean isSalaryClient) {
        log.info("Calculation depending on status of salary client");

        if (isSalaryClient) {
            currentRate = currentRate.subtract(SALARY_CLIENT_PERCENT);
            log.info("Salary client. Rate reduced by 1");
        } else {
            log.info("Not salary client. Rate not changed");
        }
    }

    private void calculateRateDependingOnGenderAndAge(Gender gender, Integer age) {
        log.info("Calculation depending on gender and age");

        BigDecimal currentRateBeforeChanges = currentRate;
        switch (gender) {
            case FEMALE -> {
                currentRate = age > 35 && age < 60 ? currentRate.subtract(FEMALE_AGE_BETWEEN_35_AND_60_PERCENT) : currentRate;
                log.info("Gender - female, age - {}. Rate reduced by 3", age);
            }
            case MALE -> {
                currentRate = age > 30 && age < 55 ? currentRate.subtract(MALE_AGE_BETWEEN_30_AND_55_PERCENT) : currentRate;
                log.info("Gender - male, age - {}. Rate reduced by 3", age);
            }
            case NON_BINARY -> {
                currentRate = currentRate.add(NON_BINARY_PERCENT);
                log.info("Gender - non binary. Rate increased by 3");
            }
        }

        currentRate = isCurrentRateGreaterThanMinimum(currentRate) ? currentRate : currentRateBeforeChanges;
    }

    /**
     * Расчет суммы тела кредита
     */
    @Override
    public BigDecimal calculateTotalAmount(BigDecimal amount, Boolean isInsuranceEnabled) {
        return isInsuranceEnabled ? amount.add(calculateInsuranceCost(amount)) : amount;
    }

    private BigDecimal calculateInsuranceCost(BigDecimal amount) {
        return amount.
                multiply(insuranceRate).
                divide(PERCENT, 2, RoundingMode.HALF_EVEN);
    }

    /**
     * Расчет ежемесячного платежа
     * P = S * (i + ( i/ (1+i)^n- 1 ), где
     * P – ежемесячный платёж по аннуитетному кредиту;
     * S – сумма кредита;
     * i – ежемесячная процентная ставка (= годовая процентная ставка/100/12);
     * n – срок, на который берётся кредит
     */
    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, Integer term, BigDecimal rate) {
        log.info("Monthly payment calculation");

        BigDecimal monthlyRate = calculateMonthlyRate(rate);
        log.info("Monthly rate {}", monthlyRate);

        BigDecimal annuityFactor = monthlyRate.multiply((BigDecimal.ONE.add(monthlyRate)).pow(term)).
                divide((BigDecimal.ONE.add(monthlyRate)).pow(term).subtract(BigDecimal.ONE), SCALE, RoundingMode.HALF_EVEN);
        log.info("Annuity factor {}", annuityFactor);

        BigDecimal monthlyPayment = totalAmount.multiply(annuityFactor).setScale(2, RoundingMode.HALF_EVEN);
        log.info("Monthly payment {}", monthlyPayment);

        return monthlyPayment;
    }

    private BigDecimal calculateMonthlyRate(BigDecimal rate) {
        return rate.
                divide(MONTHS_IN_YEAR, SCALE, RoundingMode.HALF_EVEN).
                divide(PERCENT, SCALE, RoundingMode.HALF_EVEN);
    }

    /**
     * Расчет графика ежемесячных платежей
     * <p>
     * Расчёт процентов по аннуитетным платежам
     * In = Sn * i
     * In – сумма в аннуитетном платеже, которая идёт на погашение процентов по кредиту;
     * Sn – сумма оставшейся задолженности по кредиту (остаток по кредиту);
     * i – ежемесячная процентная ставка
     * Расчёт доли тела кредита в аннуитетных платежах
     * S = P - In
     * S – сумма в аннуитетном платеже, которая идёт на погашение тела кредита;
     * P – ежемесячный аннуитетный платёж;
     * In – сумма в аннуитетном платеже, которая идёт на погашение процентов по кредиту.
     * Расчет долга на конец месяца
     * Sn2 = Sn1 - S
     * Sn2 – долг на конец месяца по аннуитетному кредиту;
     * Sn1 – сумма текущей задолженности по кредиту;
     * S – сумма в аннуитетном платеже, которая идёт на погашение тела кредита.
     */
    @Override
    public List<PaymentScheduleElement> scheduleMonthlyPayments(Integer term, BigDecimal amount, BigDecimal monthlyPayment, BigDecimal rate) {
        List<PaymentScheduleElement> payments = new ArrayList<>();
        payments.add(PaymentScheduleElement.builder().
                number(0).
                date(LocalDate.now()).
                totalPayment(amount.negate()).
                interestPayment(BigDecimal.ZERO).
                debtPayment(BigDecimal.ZERO).
                remainingDebt(amount).build());

        final BigDecimal monthlyRate = calculateMonthlyRate(rate);

        for (int i = 1; i <= term; i++) {
            PaymentScheduleElement previousPayment = payments.get(i - 1);

            LocalDate date = previousPayment.getDate().plusMonths(1);
            BigDecimal remainingDebt = previousPayment.getRemainingDebt();

            BigDecimal interestPayment = remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal debtPayment;
            if (i == term) {
                debtPayment = remainingDebt;
                monthlyPayment = interestPayment.add(debtPayment);
            } else debtPayment = monthlyPayment.subtract(interestPayment).setScale(2, RoundingMode.HALF_EVEN);
            remainingDebt = remainingDebt.subtract(debtPayment);

            payments.add(PaymentScheduleElement.builder().
                    number(i).
                    date(date).
                    totalPayment(monthlyPayment).
                    interestPayment(interestPayment).
                    debtPayment(debtPayment).
                    remainingDebt(remainingDebt).build());
        }
        return payments;
    }

    /**
     * Расчет ПСК
     * ПСК = (S/S0 - 1) / n * 100
     * ПСК – полная стоимость кредита
     * S – общая сумма всех выплат по кредиту (включая страховку);
     * S0 – сумма выданного кредита;
     * n – срок кредитования (в годах)
     */
    @Override
    public BigDecimal calculatePsk(BigDecimal monthlyPayment, BigDecimal amount, Integer term) {
        log.info("PSK calculation");

        BigDecimal totalAmountOfPayments = monthlyPayment.multiply(BigDecimal.valueOf(term));
        log.info("Total amount of all payments {}", totalAmountOfPayments);
        BigDecimal termInYears = BigDecimal.valueOf(term).divide(MONTHS_IN_YEAR, SCALE, RoundingMode.HALF_EVEN);
        log.info("Term in years {}", termInYears);

        BigDecimal psk = totalAmountOfPayments.divide(amount, SCALE, RoundingMode.HALF_EVEN).subtract(BigDecimal.ONE).
                divide(termInYears, SCALE, RoundingMode.HALF_EVEN).
                multiply(PERCENT).
                setScale(3, RoundingMode.HALF_EVEN);
        log.info("PSK {}", psk);

        return psk;
    }
}

