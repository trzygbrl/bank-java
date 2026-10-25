import java.awt.GraphicsEnvironment;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 * Automated GUI smoke test for Simple Bank.
 * Validates DepositView, WithdrawView, BankAccount state, and HistoryView
 * on the Swing Event Dispatch Thread (EDT) without using java.awt.Robot.
 * Skips gracefully if running in a headless environment.
 */
public class GuiSmokeTest {

    private static int passed = 0;
    private static int failed = 0;

    /**
     * Entry point for GUI smoke testing on EDT. Exits gracefully if headless.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("SKIPPED: Running in headless environment.");
            System.exit(0);
        }

        try {
            SwingUtilities.invokeAndWait(GuiSmokeTest::runSmokeTests);
        } catch (Throwable t) {
            System.err.println("FAIL: Unexpected exception during GuiSmokeTest: " + t.getMessage());
            t.printStackTrace();
            System.exit(1);
        }

        System.out.println("---------------------------");
        System.out.println("GuiSmokeTest Summary: " + passed + " passed, " + failed + " failed.");
        if (failed > 0) {
            System.exit(1);
        }
        System.exit(0);
    }

    /**
     * Executes the sequence of automated GUI smoke test cases.
     */
    private static void runSmokeTests() {
        System.out.println("--- Starting GuiSmokeTest ---");
        BankFrame frame = new BankFrame();
        BankAccount account = frame.getBankAccount();
        DepositView depositView = frame.getDepositView();
        WithdrawView withdrawView = frame.getWithdrawView();
        HistoryView historyView = frame.getHistoryView();

        // Initial launch check (500000 centavos)
        check("Initial account balance is 500000", account.getBalance() == 500000L);
        check("Initial history record count is 0", account.getTransactions().isEmpty());

        // 1. Invalid inputs on DepositView (0, -50, abc, blank, 10.999)
        String[] invalidInputs = {"0", "-50", "abc", "", "10.999"};
        for (String invalid : invalidInputs) {
            frame.handleDeposit();
            depositView.setAmountText(invalid);
            depositView.submitDeposit();
            check("Deposit invalid '" + invalid + "' sets error state", depositView.isErrorState());
            check("Deposit invalid '" + invalid + "' balance unchanged", account.getBalance() == 500000L);
            check("Deposit invalid '" + invalid + "' adds no history record", account.getTransactions().isEmpty());
        }

        // 2. Deposit 1000 gives balance 600000
        frame.handleDeposit();
        depositView.setAmountText("1000");
        depositView.submitDeposit();
        check("Deposit 1000 gives balance 600000", account.getBalance() == 600000L);
        check("Deposit 1000 clears error state", !depositView.isErrorState());
        check("Deposit 1000 clears field", depositView.getAmountText().isEmpty());
        check("Deposit 1000 adds 1 history record", account.getTransactions().size() == 1);

        // 3. Invalid inputs on WithdrawView (0, -50, abc, blank, 10.999)
        for (String invalid : invalidInputs) {
            frame.handleWithdraw();
            withdrawView.setAmountText(invalid);
            withdrawView.submitWithdraw();
            check("Withdraw invalid '" + invalid + "' sets error state", withdrawView.isErrorState());
            check("Withdraw invalid '" + invalid + "' balance unchanged", account.getBalance() == 600000L);
            check("Withdraw invalid '" + invalid + "' adds no history record", account.getTransactions().size() == 1);
        }

        // 4. Withdraw 2000 gives balance 400000
        frame.handleWithdraw();
        withdrawView.setAmountText("2000");
        withdrawView.submitWithdraw();
        check("Withdraw 2000 gives balance 400000", account.getBalance() == 400000L);
        check("Withdraw 2000 clears error state", !withdrawView.isErrorState());
        check("Withdraw 2000 clears field", withdrawView.getAmountText().isEmpty());
        check("Withdraw 2000 results in 2 history records", account.getTransactions().size() == 2);

        // 5. Withdraw 99999999 is rejected and changes nothing
        frame.handleWithdraw();
        withdrawView.setAmountText("99999999");
        withdrawView.submitWithdraw();
        check("Overdraft withdraw 99999999 sets error state", withdrawView.isErrorState());
        check("Overdraft withdraw 99999999 balance unchanged at 400000", account.getBalance() == 400000L);
        check("Overdraft withdraw 99999999 history count unchanged at 2", account.getTransactions().size() == 2);

        // 6. History has exactly 2 records with correct balanceAfter values
        frame.handleViewHistory();
        List<Transaction> txs = account.getTransactions();
        check("History record count is exactly 2", txs.size() == 2);
        check("History table row count is exactly 2", historyView.getTableRowCount() == 2);

        Transaction tx1 = txs.get(0);
        check("Record 1 is DEPOSIT", tx1.type() == TransactionType.DEPOSIT);
        check("Record 1 amount is 100000", tx1.amountCentavos() == 100000L);
        check("Record 1 balanceAfter is 600000", tx1.balanceAfterCentavos() == 600000L);

        Transaction tx2 = txs.get(1);
        check("Record 2 is WITHDRAWAL", tx2.type() == TransactionType.WITHDRAWAL);
        check("Record 2 amount is 200000", tx2.amountCentavos() == 200000L);
        check("Record 2 balanceAfter is 400000", tx2.balanceAfterCentavos() == 400000L);

        // Formatted table cell values in HistoryView
        check("Table row 0 No. is 1", Integer.valueOf(1).equals(historyView.getCellValue(0, 0)));
        check("Table row 0 Type is Deposit", "Deposit".equals(historyView.getCellValue(0, 1)));
        check("Table row 0 Amount is \u20B11,000.00", "\u20B11,000.00".equals(historyView.getCellValue(0, 2)));
        check("Table row 0 Balance After is \u20B16,000.00", "\u20B16,000.00".equals(historyView.getCellValue(0, 3)));

        check("Table row 1 No. is 2", Integer.valueOf(2).equals(historyView.getCellValue(1, 0)));
        check("Table row 1 Type is Withdrawal", "Withdrawal".equals(historyView.getCellValue(1, 1)));
        check("Table row 1 Amount is \u20B12,000.00", "\u20B12,000.00".equals(historyView.getCellValue(1, 2)));
        check("Table row 1 Balance After is \u20B14,000.00", "\u20B14,000.00".equals(historyView.getCellValue(1, 3)));

        frame.dispose();
    }

    /**
     * Asserts that a condition is true and logs the result.
     *
     * @param desc description of test check
     * @param condition boolean condition to test
     */
    private static void check(String desc, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + desc);
            passed++;
        } else {
            System.err.println("FAIL: " + desc);
            failed++;
        }
    }
}
