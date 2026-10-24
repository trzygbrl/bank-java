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

    public long getBalance() {
        return balanceCentavos;
    }

    public void deposit(long amountCentavos) {
        balanceCentavos += amountCentavos;
        recordTransaction(TransactionType.DEPOSIT, amountCentavos);
    }

    public boolean withdraw(long amountCentavos) {
        if (amountCentavos > balanceCentavos) {
            return false;
        }
        balanceCentavos -= amountCentavos;
        recordTransaction(TransactionType.WITHDRAWAL, amountCentavos);
        return true;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    private void recordTransaction(TransactionType type, long amountCentavos) {
        transactions.add(new Transaction(type, amountCentavos, balanceCentavos));
    }
}
