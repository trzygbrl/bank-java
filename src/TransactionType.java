/**
 * Represents the type of a banking transaction.
 */
public enum TransactionType {
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal");

    private final String label;

    TransactionType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
