import java.util.List;

/**
 * Logic test runner covering MoneyUtil formatting, amount validation rules,
 * AmountResult behavior, and BankAccount regression tests.
 */
public final class LogicTests {
    private static int passedCount = 0;
    private static int failedCount = 0;

    /**
     * Test runner main method. Executes all logic test groups and exits with status code.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("--- Starting LogicTests ---");

        testAmountResult();
        testFormatMoney();
        testValidateAmountRequiredCases();
        testValidateAmountEdgeCases();
        testBankAccountRegression();

        System.out.println("---------------------------");
        System.out.println("LogicTests Summary: " + passedCount + " passed, " + failedCount + " failed.");

        if (failedCount > 0) {
            System.exit(1);
        }
    }

    /**
     * Asserts that a boolean condition is true.
     *
     * @param description test case description
     * @param condition the condition that must evaluate to true
     */
    private static void check(String description, boolean condition) {
        assert condition : description;
        if (condition) {
            System.out.println("PASS: " + description);
            passedCount++;
        } else {
            System.out.println("FAIL: " + description);
            failedCount++;
        }
    }

    /**
     * Asserts that an actual value equals an expected value.
     *
     * @param description test case description
     * @param actual actual value obtained
     * @param expected expected value
     */
    private static void checkEqual(String description, Object actual, Object expected) {
        boolean match = (actual == null && expected == null) || (actual != null && actual.equals(expected));
        assert match : description + " (expected: " + expected + ", actual: " + actual + ")";
        if (match) {
            System.out.println("PASS: " + description);
            passedCount++;
        } else {
            System.out.println("FAIL: " + description + " (expected: [" + expected + "], actual: [" + actual + "])");
            failedCount++;
        }
    }

    /**
     * Tests AmountResult factory methods, error states, and accessors.
     */
    private static void testAmountResult() {
        AmountResult okRes = AmountResult.ok(12345L);
        check("AmountResult.ok isValid", okRes.isValid());
        checkEqual("AmountResult.ok getCentavos", okRes.getCentavos(), 12345L);
        checkEqual("AmountResult.ok errorMessage is null", okRes.getErrorMessage(), null);

        AmountResult zeroOk = AmountResult.ok(0L);
        check("AmountResult.ok(0) isValid is true", zeroOk.isValid());
        checkEqual("AmountResult.ok(0) getCentavos is 0", zeroOk.getCentavos(), 0L);

        AmountResult errRes = AmountResult.error("Test error");
        check("AmountResult.error isValid is false", !errRes.isValid());
        checkEqual("AmountResult.error getErrorMessage", errRes.getErrorMessage(), "Test error");
    }

    /**
     * Tests MoneyUtil.formatMoney against required cases from PRD Section 7.
     */
    private static void testFormatMoney() {
        checkEqual("formatMoney 0", MoneyUtil.formatMoney(0L), "\u20B10.00");
        checkEqual("formatMoney 5", MoneyUtil.formatMoney(5L), "\u20B10.05");
        checkEqual("formatMoney 100", MoneyUtil.formatMoney(100L), "\u20B11.00");
        checkEqual("formatMoney 500000", MoneyUtil.formatMoney(500_000L), "\u20B15,000.00");
        checkEqual("formatMoney 123456789", MoneyUtil.formatMoney(123_456_789L), "\u20B11,234,567.89");
        checkEqual("formatMoney 100000000000", MoneyUtil.formatMoney(100_000_000_000L), "\u20B11,000,000,000.00");
    }

    /**
     * Tests MoneyUtil.validateAmount against core required functional tests TC-05 through TC-10.
     */
    private static void testValidateAmountRequiredCases() {
        // Valid inputs
        AmountResult r1 = MoneyUtil.validateAmount("1000");
        check("validateAmount '1000' isValid", r1.isValid());
        checkEqual("validateAmount '1000' centavos", r1.getCentavos(), 100_000L);

        AmountResult r2 = MoneyUtil.validateAmount("250.75");
        check("validateAmount '250.75' isValid", r2.isValid());
        checkEqual("validateAmount '250.75' centavos", r2.getCentavos(), 25_075L);

        AmountResult r3 = MoneyUtil.validateAmount("0.5");
        check("validateAmount '0.5' isValid", r3.isValid());
        checkEqual("validateAmount '0.5' centavos", r3.getCentavos(), 50L);

        AmountResult r4 = MoneyUtil.validateAmount(" 500 ");
        check("validateAmount ' 500 ' isValid", r4.isValid());
        checkEqual("validateAmount ' 500 ' centavos", r4.getCentavos(), 50_000L);

        // Reject zero and negative amounts
        AmountResult rZero = MoneyUtil.validateAmount("0");
        check("validateAmount '0' is invalid", !rZero.isValid());
        checkEqual("validateAmount '0' error message", rZero.getErrorMessage(), MoneyUtil.ERROR_MUST_BE_POSITIVE);

        AmountResult rZeroDec = MoneyUtil.validateAmount("0.00");
        check("validateAmount '0.00' is invalid", !rZeroDec.isValid());
        checkEqual("validateAmount '0.00' error message", rZeroDec.getErrorMessage(), MoneyUtil.ERROR_MUST_BE_POSITIVE);

        AmountResult rNegZero = MoneyUtil.validateAmount("-0");
        check("validateAmount '-0' is invalid", !rNegZero.isValid());
        checkEqual("validateAmount '-0' error message", rNegZero.getErrorMessage(), MoneyUtil.ERROR_MUST_BE_POSITIVE);

        AmountResult rNegFifty = MoneyUtil.validateAmount("-50");
        check("validateAmount '-50' is invalid", !rNegFifty.isValid());
        checkEqual("validateAmount '-50' error message", rNegFifty.getErrorMessage(), MoneyUtil.ERROR_MUST_BE_POSITIVE);

        // Invalid number syntax
        AmountResult rAbc = MoneyUtil.validateAmount("abc");
        check("validateAmount 'abc' is invalid", !rAbc.isValid());
        checkEqual("validateAmount 'abc' error message", rAbc.getErrorMessage(), MoneyUtil.ERROR_INVALID_NUMBER);

        AmountResult rEmpty = MoneyUtil.validateAmount("");
        check("validateAmount '' is invalid", !rEmpty.isValid());
        checkEqual("validateAmount '' error message", rEmpty.getErrorMessage(), MoneyUtil.ERROR_INVALID_NUMBER);

        AmountResult rSci = MoneyUtil.validateAmount("1e3");
        check("validateAmount '1e3' is invalid", !rSci.isValid());
        checkEqual("validateAmount '1e3' error message", rSci.getErrorMessage(), MoneyUtil.ERROR_INVALID_NUMBER);

        AmountResult rDotFive = MoneyUtil.validateAmount(".5");
        check("validateAmount '.5' is invalid", !rDotFive.isValid());
        checkEqual("validateAmount '.5' error message", rDotFive.getErrorMessage(), MoneyUtil.ERROR_INVALID_NUMBER);

        AmountResult rFiveDot = MoneyUtil.validateAmount("5.");
        check("validateAmount '5.' is invalid", !rFiveDot.isValid());
        checkEqual("validateAmount '5.' error message", rFiveDot.getErrorMessage(), MoneyUtil.ERROR_INVALID_NUMBER);

        AmountResult rComma = MoneyUtil.validateAmount("1,000");
        check("validateAmount '1,000' is invalid", !rComma.isValid());
        checkEqual("validateAmount '1,000' error message", rComma.getErrorMessage(), MoneyUtil.ERROR_INVALID_NUMBER);

        // More than 2 decimal places
        AmountResult rDecimals = MoneyUtil.validateAmount("10.999");
        check("validateAmount '10.999' is invalid", !rDecimals.isValid());
        checkEqual("validateAmount '10.999' error message", rDecimals.getErrorMessage(), MoneyUtil.ERROR_MAX_TWO_DECIMALS);

        // Too large
        AmountResult rLarge = MoneyUtil.validateAmount("99999999999");
        check("validateAmount '99999999999' is invalid", !rLarge.isValid());
        checkEqual("validateAmount '99999999999' error message", rLarge.getErrorMessage(), MoneyUtil.ERROR_TOO_LARGE);

        // Null input
        AmountResult rNull = MoneyUtil.validateAmount(null);
        check("validateAmount null is invalid", !rNull.isValid());
        checkEqual("validateAmount null error message", rNull.getErrorMessage(), MoneyUtil.ERROR_INVALID_NUMBER);
    }

    /**
     * Tests MoneyUtil.validateAmount edge cases including large inputs, bounds, and precedence.
     */
    private static void testValidateAmountEdgeCases() {
        // Very long numeric input without throwing
        AmountResult rHugeDigits = MoneyUtil.validateAmount("1234567890123456789012345678901234567890");
        check("validateAmount 40-digit number does not throw and is invalid", !rHugeDigits.isValid());
        checkEqual("validateAmount 40-digit number error is too-large", rHugeDigits.getErrorMessage(), MoneyUtil.ERROR_TOO_LARGE);

        // Numeric string larger than Long.MAX_VALUE
        AmountResult rOverLongMax = MoneyUtil.validateAmount("99999999999999999999999999999999999999");
        check("validateAmount > Long.MAX_VALUE is invalid", !rOverLongMax.isValid());
        checkEqual("validateAmount > Long.MAX_VALUE error is too-large", rOverLongMax.getErrorMessage(), MoneyUtil.ERROR_TOO_LARGE);

        // Maximum supported value exactly (1,000,000,000.00 -> 100_000_000_000L centavos)
        AmountResult rMaxWithDec = MoneyUtil.validateAmount("1000000000.00");
        check("validateAmount '1000000000.00' isValid", rMaxWithDec.isValid());
        checkEqual("validateAmount '1000000000.00' centavos", rMaxWithDec.getCentavos(), MoneyUtil.MAX_AMOUNT_CENTAVOS);

        // Maximum supported value without decimals (1000000000 -> 100_000_000_000L centavos)
        AmountResult rMaxNoDec = MoneyUtil.validateAmount("1000000000");
        check("validateAmount '1000000000' isValid", rMaxNoDec.isValid());
        checkEqual("validateAmount '1000000000' centavos", rMaxNoDec.getCentavos(), MoneyUtil.MAX_AMOUNT_CENTAVOS);

        // Value above supported maximum
        AmountResult rAboveMax = MoneyUtil.validateAmount("1000000000.01");
        check("validateAmount '1000000000.01' is invalid", !rAboveMax.isValid());
        checkEqual("validateAmount '1000000000.01' error is too-large", rAboveMax.getErrorMessage(), MoneyUtil.ERROR_TOO_LARGE);

        // Leading zeros on whole part
        AmountResult rLeadingZeros = MoneyUtil.validateAmount("00500.50");
        check("validateAmount '00500.50' isValid", rLeadingZeros.isValid());
        checkEqual("validateAmount '00500.50' centavos", rLeadingZeros.getCentavos(), 50_050L);

        // Precedence: negative value with multiple decimals must reject in step 3 (must-be-positive)
        AmountResult rNegDecimals = MoneyUtil.validateAmount("-10.999");
        check("validateAmount '-10.999' is invalid", !rNegDecimals.isValid());
        checkEqual("validateAmount '-10.999' check precedence is must-be-positive", rNegDecimals.getErrorMessage(), MoneyUtil.ERROR_MUST_BE_POSITIVE);

        // Very long all-zero input does not throw and rejects as must-be-positive
        AmountResult rLongZero = MoneyUtil.validateAmount("000000000000000000000000000000000000");
        check("validateAmount long zeros is invalid", !rLongZero.isValid());
        checkEqual("validateAmount long zeros error is must-be-positive", rLongZero.getErrorMessage(), MoneyUtil.ERROR_MUST_BE_POSITIVE);

        // Blank whitespace string
        AmountResult rWhitespace = MoneyUtil.validateAmount("   ");
        check("validateAmount '   ' is invalid", !rWhitespace.isValid());
        checkEqual("validateAmount '   ' error is invalid-number", rWhitespace.getErrorMessage(), MoneyUtil.ERROR_INVALID_NUMBER);

        // Leading plus sign rejected by syntax
        AmountResult rPlusSign = MoneyUtil.validateAmount("+500");
        check("validateAmount '+500' is invalid", !rPlusSign.isValid());
        checkEqual("validateAmount '+500' error is invalid-number", rPlusSign.getErrorMessage(), MoneyUtil.ERROR_INVALID_NUMBER);
    }

    /**
     * Tests BankAccount state transitions, starting balance, deposits, withdrawals, and immutability.
     */
    private static void testBankAccountRegression() {
        BankAccount account = new BankAccount();

        // 1. Starting balance is 500,000 centavos
        checkEqual("BankAccount fresh balance is 500000", account.getBalance(), 500_000L);

        // 2. Initial transactions history is empty
        checkEqual("BankAccount initial history is empty", account.getTransactions().size(), 0);

        // 3. Deposit 100,000 centavos
        account.deposit(100_000L);
        checkEqual("BankAccount balance after deposit(100000)", account.getBalance(), 600_000L);
        checkEqual("BankAccount transaction count after deposit", account.getTransactions().size(), 1);

        // 4. Withdraw 200,000 centavos
        boolean withdrawOk = account.withdraw(200_000L);
        check("BankAccount withdraw(200000) returned true", withdrawOk);
        checkEqual("BankAccount balance after withdraw(200000)", account.getBalance(), 400_000L);
        checkEqual("BankAccount transaction count after withdraw", account.getTransactions().size(), 2);

        // 5. Transaction history records verification
        List<Transaction> txs = account.getTransactions();
        Transaction t1 = txs.get(0);
        checkEqual("Transaction 1 type is DEPOSIT", t1.type(), TransactionType.DEPOSIT);
        checkEqual("Transaction 1 amount is 100000", t1.amountCentavos(), 100_000L);
        checkEqual("Transaction 1 balanceAfter is 600000", t1.balanceAfterCentavos(), 600_000L);

        Transaction t2 = txs.get(1);
        checkEqual("Transaction 2 type is WITHDRAWAL", t2.type(), TransactionType.WITHDRAWAL);
        checkEqual("Transaction 2 amount is 200000", t2.amountCentavos(), 200_000L);
        checkEqual("Transaction 2 balanceAfter is 400000", t2.balanceAfterCentavos(), 400_000L);

        // 6. Overdraft attempt
        boolean overdraftResult = account.withdraw(99_999_999L);
        check("BankAccount overdraft withdraw(99999999) returned false", !overdraftResult);

        // 7. Balance and history unchanged after overdraft
        checkEqual("BankAccount balance after overdraft unchanged at 400000", account.getBalance(), 400_000L);
        checkEqual("BankAccount history count after overdraft unchanged at 2", account.getTransactions().size(), 2);

        // 8. Unmodifiable transaction list
        boolean threwUnsupported = false;
        try {
            account.getTransactions().add(new Transaction(TransactionType.DEPOSIT, 10_000L, 410_000L));
        } catch (UnsupportedOperationException e) {
            threwUnsupported = true;
        }
        check("getTransactions() is unmodifiable (throws UnsupportedOperationException)", threwUnsupported);
        checkEqual("BankAccount transaction history unaltered after add attempt", account.getTransactions().size(), 2);
    }
}
