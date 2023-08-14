package ru.neoflex.conveyor.util.exceptions;

public class LoanRejectionException extends RuntimeException {
    public LoanRejectionException() {
        super("Отказ в одобрении кредита");
    }
}
