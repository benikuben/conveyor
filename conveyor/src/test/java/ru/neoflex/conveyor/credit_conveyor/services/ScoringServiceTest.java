package ru.neoflex.conveyor.credit_conveyor.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.neoflex.conveyor.dtos.EmploymentDTO;
import ru.neoflex.conveyor.dtos.PaymentScheduleElement;
import ru.neoflex.conveyor.dtos.ScoringDataDTO;
import ru.neoflex.conveyor.services.ScoringService;
import ru.neoflex.conveyor.util.EmploymentStatus;
import ru.neoflex.conveyor.util.Gender;
import ru.neoflex.conveyor.util.MaritalStatus;
import ru.neoflex.conveyor.util.Position;
import ru.neoflex.conveyor.util.exceptions.LoanRejectionException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScoringServiceTest {
    @Autowired
    private ScoringService scoringService;

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public ScoringService scoringServiceBean() {
            return new ScoringService();
        }
    }

    @Test
    void performScoring() {
        ScoringDataDTO data = new ScoringDataDTO();
        EmploymentDTO employment = new EmploymentDTO();
        data.setBirthdate(LocalDate.now());
        data.setAmount(BigDecimal.valueOf(10000));
        data.setIsInsuranceEnabled(false);

        //isEmploymentStatusUnemployed
        employment.setEmploymentStatus(EmploymentStatus.UNEMPLOYED);
        data.setEmployment(employment);
        assertThrows(LoanRejectionException.class, () -> scoringService.performScoring(data));

        //isAmountMoreThanTwentySalaries
        employment.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        employment.setSalary(BigDecimal.valueOf(100));
        assertThrows(LoanRejectionException.class, () -> scoringService.performScoring(data));

        //isAgeLessThanTwentyOrMoreThanSixty
        employment.setSalary(BigDecimal.valueOf(1000));
        assertThrows(LoanRejectionException.class, () -> scoringService.performScoring(data));

        //isTotalWorkExperienceLessThanTwelve
        data.setBirthdate(LocalDate.of(2000, 1, 1));
        employment.setWorkExperienceTotal(1);
        assertThrows(LoanRejectionException.class, () -> scoringService.performScoring(data));

        //isCurrentWorkExperienceLessThanThree
        employment.setWorkExperienceTotal(13);
        employment.setWorkExperienceCurrent(1);
        assertThrows(LoanRejectionException.class, () -> scoringService.performScoring(data));
    }

    @Test
    void calculateRate() {
        ScoringDataDTO data = new ScoringDataDTO();
        EmploymentDTO employment = new EmploymentDTO();

        /*
         * Базовая ставка - 17
         * Добавлена страховка -> ставка уменьшается на 3
         * Зарплатный клиент -> ставка уменьшается на 1
         * Рабочий статус: Самозанятый → ставка увеличивается на 1
         * Позиция на работе: Менеджер среднего звена → ставка уменьшается на 2
         * Семейное положение: Замужем/женат → ставка уменьшается на 3
         * Количество иждивенцев больше 1 → ставка увеличивается на 1
         * Пол: Женщина, возраст от 35 до 60 лет → ставка уменьшается на 3
         */
        data.setIsInsuranceEnabled(true);
        data.setIsSalaryClient(true);
        employment.setEmploymentStatus(EmploymentStatus.SELF_EMPLOYED);
        employment.setPosition(Position.MID_MANAGER);
        data.setMaritalStatus(MaritalStatus.MARRIED);
        data.setDependentAmount(2);
        data.setGender(Gender.FEMALE);
        data.setBirthdate(LocalDate.of(1980, 1, 1));
        data.setEmployment(employment);

        BigDecimal expectedRate1 = BigDecimal.valueOf(7);
        BigDecimal actualRate1 = scoringService.calculateRate(data);
        assertEquals(expectedRate1, actualRate1);

        /*
         * Базовая ставка - 17
         * Рабочий статус: Владелец бизнеса → ставка увеличивается на 3
         * Позиция на работе: Топ-менеджер → ставка уменьшается на 4
         * Семейное положение: Разведен → ставка увеличивается на 1
         * Пол:  Не бинарный → ставка увеличивается на 3
         */
        data.setIsInsuranceEnabled(false);
        data.setIsSalaryClient(false);
        data.setDependentAmount(0);
        employment.setEmploymentStatus(EmploymentStatus.BUSINESS_OWNER);
        employment.setPosition(Position.TOP_MANAGER);
        data.setMaritalStatus(MaritalStatus.DIVORCED);
        data.setGender(Gender.NON_BINARY);
        data.setEmployment(employment);

        BigDecimal expectedRate2 = BigDecimal.valueOf(20);
        BigDecimal actualRate2 = scoringService.calculateRate(data);
        assertEquals(expectedRate2, actualRate2);
    }

    @Test
    void testCalculateRate() {
        BigDecimal expectedRate1 = BigDecimal.valueOf(17);
        BigDecimal actualRate1 = scoringService.calculateRate(false, false);
        assertEquals(expectedRate1, actualRate1);

        BigDecimal expectedRate2 = BigDecimal.valueOf(16);
        BigDecimal actualRate2 = scoringService.calculateRate(false, true);
        assertEquals(expectedRate2, actualRate2);

        BigDecimal expectedRate3 = BigDecimal.valueOf(14);
        BigDecimal actualRate3 = scoringService.calculateRate(true, false);
        assertEquals(expectedRate3, actualRate3);

        BigDecimal expectedRate4 = BigDecimal.valueOf(13);
        BigDecimal actualRate4 = scoringService.calculateRate(true, true);
        assertEquals(expectedRate4, actualRate4);
    }

    @Test
    void calculateTotalAmount() {
        BigDecimal actualAmount1 = scoringService.calculateTotalAmount(BigDecimal.valueOf(10000), true);
        assertEquals(BigDecimal.valueOf(10150).setScale(2), actualAmount1);

        BigDecimal actualAmount2 = scoringService.calculateTotalAmount(BigDecimal.valueOf(10000), false);
        assertEquals(BigDecimal.valueOf(10000), actualAmount2);
    }

    @Test
    void calculateMonthlyPayment() {
        BigDecimal expectedMonthlyPayment = BigDecimal.valueOf(1750.27);
        BigDecimal actualMonthlyPayment = scoringService.calculateMonthlyPayment(BigDecimal.valueOf(10000), 6, BigDecimal.valueOf(17));
        assertEquals(expectedMonthlyPayment, actualMonthlyPayment);
    }

    @Test
    void scheduleMonthlyPayments() {
        Integer term = 6;
        BigDecimal amount = BigDecimal.valueOf(10000);
        BigDecimal monthlyPayment = BigDecimal.valueOf(1750.27);
        BigDecimal rate = BigDecimal.valueOf(17);
        LocalDate date1 = LocalDate.now().plusMonths(1);
        LocalDate date2 = date1.plusMonths(1);
        LocalDate date3 = date2.plusMonths(1);
        LocalDate date4 = date3.plusMonths(1);
        LocalDate date5 = date4.plusMonths(1);
        LocalDate date6 = date5.plusMonths(1);
        List<PaymentScheduleElement> expectedPayments = List.of(
                new PaymentScheduleElement(0, LocalDate.now(), amount.negate(), BigDecimal.ZERO, BigDecimal.ZERO, amount),
                new PaymentScheduleElement(1, date1, monthlyPayment, BigDecimal.valueOf(141.67), BigDecimal.valueOf(1608.60).setScale(2), BigDecimal.valueOf(8391.40).setScale(2)),
                new PaymentScheduleElement(2, date2, monthlyPayment, BigDecimal.valueOf(118.88), BigDecimal.valueOf(1631.39), BigDecimal.valueOf(6760.01)),
                new PaymentScheduleElement(3, date3, monthlyPayment, BigDecimal.valueOf(95.77), BigDecimal.valueOf(1654.50).setScale(2), BigDecimal.valueOf(5105.51)),
                new PaymentScheduleElement(4, date4, monthlyPayment, BigDecimal.valueOf(72.33), BigDecimal.valueOf(1677.94), BigDecimal.valueOf(3427.57)),
                new PaymentScheduleElement(5, date5, monthlyPayment, BigDecimal.valueOf(48.56), BigDecimal.valueOf(1701.71), BigDecimal.valueOf(1725.86)),
                new PaymentScheduleElement(6, date6, BigDecimal.valueOf(1750.31), BigDecimal.valueOf(24.45), BigDecimal.valueOf(1725.86), BigDecimal.valueOf(0.00).setScale(2))
        );

        List<PaymentScheduleElement> actualPayments = scoringService.scheduleMonthlyPayments(term, amount, monthlyPayment, rate);

        assertEquals(expectedPayments, actualPayments);
    }

    @Test
    void calculatePsk() {
        Integer term = 6;
        BigDecimal amount = BigDecimal.valueOf(10000);
        BigDecimal monthlyPayment = BigDecimal.valueOf(1750.27);
        BigDecimal expectedPsk = BigDecimal.valueOf(10.032);

        BigDecimal actualPsk = scoringService.calculatePsk(monthlyPayment, amount, term);

        assertEquals(expectedPsk, actualPsk);
    }
}