package ru.neoflex.conveyor.exceptions;

public class LoanRejectionException extends RuntimeException {
    public LoanRejectionException() {
        super("Отказ в одобрении кредита");
    }
}
