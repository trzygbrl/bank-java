/**
 * Represents an immutable record of a completed banking transaction.
 *
 * @param type the transaction type (DEPOSIT or WITHDRAWAL)
 * @param amountCentavos the transaction amount in centavos
 * @param balanceAfterCentavos the post-transaction account balance in centavos
 */
public record Transaction(
    TransactionType type,
    long amountCentavos,
    long balanceAfterCentavos
) {
}
