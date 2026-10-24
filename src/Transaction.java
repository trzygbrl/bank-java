/**
 * Represents an immutable record of a completed banking transaction.
 */
public record Transaction(
    TransactionType type,
    long amountCentavos,
    long balanceAfterCentavos
) {
}
