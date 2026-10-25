/**
 * Represents the type of a banking transaction.
 */
public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal");

    private final String label;

    /**
     * Constructs a TransactionType with the specified display label.
     *
     * @param label human-readable name of the transaction type
     */
    TransactionType(String label) {
        this.label = label;
    }

    /**
     * Returns the human-readable display label for this transaction type.
     *
     * @return display label (e.g. "Deposit" or "Withdrawal")
     */
    public String getLabel() {
        return label;
    }
}
