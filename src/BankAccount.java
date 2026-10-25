import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single in-memory bank account with deposit, withdrawal, and transaction history tracking.
 */
public class BankAccount {
    public static final long STARTING_BALANCE_CENTAVOS = 500_000L;

    private long balanceCentavos = STARTING_BALANCE_CENTAVOS;
    private final ArrayList<Transaction> transactions = new ArrayList<>();

    /**
     * Returns the current balance in centavos.
     *
     * @return current balance in centavos
     */
    public long getBalance() {
        return balanceCentavos;
    }

    /**
     * Deposits the specified amount into the account and logs the transaction.
     *
     * @param amountCentavos amount to deposit in centavos
     */
    public void deposit(long amountCentavos) {
        balanceCentavos += amountCentavos;
        recordTransaction(TransactionType.DEPOSIT, amountCentavos);
    }

    /**
     * Withdraws the specified amount from the account if sufficient funds are available.
     *
     * @param amountCentavos amount to withdraw in centavos
     * @return true if withdrawal succeeded, false if insufficient funds
     */
    public boolean withdraw(long amountCentavos) {
        if (amountCentavos > balanceCentavos) {
            return false;
        }
        balanceCentavos -= amountCentavos;
        recordTransaction(TransactionType.WITHDRAWAL, amountCentavos);
        return true;
    }

    /**
     * Returns an unmodifiable list of completed transactions in chronological order.
     *
     * @return unmodifiable view of the transaction history
     */
    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    /**
     * Records a transaction with the current post-transaction balance.
     *
     * @param type transaction type (DEPOSIT or WITHDRAWAL)
     * @param amountCentavos amount involved in centavos
     */
    private void recordTransaction(TransactionType type, long amountCentavos) {
        transactions.add(new Transaction(type, amountCentavos, balanceCentavos));
    }
}
