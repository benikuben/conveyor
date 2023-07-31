package ru.neoflex.conveyor.credit_conveyor.util.exceptions;

public class LoanRejectionException extends RuntimeException {
    public LoanRejectionException() {
        super("Отказ в одобрении кредита");
    }
}
