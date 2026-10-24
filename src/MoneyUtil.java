/**
 * Utility methods and constants for currency formatting and amount validation.
 */
public final class MoneyUtil {
    public static final String CURRENCY_SYMBOL = "\u20B1";
    public static final long MAX_AMOUNT_CENTAVOS = 100_000_000_000L;

    // Exact message constants from PRD Section 10
    public static final String ERROR_INVALID_NUMBER =
            "Invalid input. Please enter a numeric amount (e.g., 500 or 250.75).";
    public static final String ERROR_MUST_BE_POSITIVE =
            "Amount must be greater than zero.";
    public static final String ERROR_MAX_TWO_DECIMALS =
            "Amount can have at most 2 decimal places.";
    public static final String ERROR_TOO_LARGE =
            "Amount is too large. The maximum per transaction is " + CURRENCY_SYMBOL + "1,000,000,000.00.";

    // Message constant aliases
    public static final String MSG_INVALID_NUMBER = ERROR_INVALID_NUMBER;
    public static final String MSG_MUST_BE_POSITIVE = ERROR_MUST_BE_POSITIVE;
    public static final String MSG_MAX_TWO_DECIMALS = ERROR_MAX_TWO_DECIMALS;
    public static final String MSG_TOO_LARGE = ERROR_TOO_LARGE;

    private MoneyUtil() {
        // Prevent instantiation of utility class
    }

    /**
     * Formats a centavo amount into currency display format with thousands separators.
     * Uses integer arithmetic and a loop to insert commas every 3 digits from the right.
     */
    public static String formatMoney(long centavos) {
        long pesos = centavos / 100L;
        long cents = centavos % 100L;
        if (cents < 0) {
            cents = -cents;
        }

        String pesoStr = Long.toString(pesos);
        StringBuilder sb = new StringBuilder();
        int len = pesoStr.length();
        for (int i = 0; i < len; i++) {
            sb.append(pesoStr.charAt(i));
            int remaining = len - 1 - i;
            if (remaining > 0 && remaining % 3 == 0) {
                sb.append(',');
            }
        }

        String centStr = (cents < 10 ? "0" : "") + cents;
        return CURRENCY_SYMBOL + sb.toString() + "." + centStr;
    }

    /**
     * Validates an amount string following PRD Section 6.5 steps 1 to 5 in exact order.
     * Never throws an exception for any input.
     */
    public static AmountResult validateAmount(String text) {
        // Step 1: Trim surrounding whitespace, safely handle null
        if (text == null) {
            return AmountResult.error(ERROR_INVALID_NUMBER);
        }
        String trimmed = text.trim();

        // Step 2: Validate numeric syntax (-?\d+(\.\d+)?)
        if (!trimmed.matches("-?\\d+(\\.\\d+)?")) {
            return AmountResult.error(ERROR_INVALID_NUMBER);
        }

        // Step 3: Reject zero and negative amounts
        if (trimmed.startsWith("-") || !containsNonZeroDigit(trimmed)) {
            return AmountResult.error(ERROR_MUST_BE_POSITIVE);
        }

        // Step 4: Reject more than two decimal places
        int dotIndex = trimmed.indexOf('.');
        if (dotIndex >= 0) {
            int decimalPlaces = trimmed.length() - dotIndex - 1;
            if (decimalPlaces > 2) {
                return AmountResult.error(ERROR_MAX_TWO_DECIMALS);
            }
        }

        // Step 5: Reject amounts that are too large (whole digits > 10 or value > MAX_AMOUNT_CENTAVOS)
        String wholePesosStr = dotIndex >= 0 ? trimmed.substring(0, dotIndex) : trimmed;
        String fractionStr = dotIndex >= 0 ? trimmed.substring(dotIndex + 1) : "";

        // Strip leading zeros
        int firstNonZero = 0;
        while (firstNonZero < wholePesosStr.length() && wholePesosStr.charAt(firstNonZero) == '0') {
            firstNonZero++;
        }
        String strippedWhole = wholePesosStr.substring(firstNonZero);

        if (strippedWhole.length() > 10) {
            return AmountResult.error(ERROR_TOO_LARGE);
        }

        // Convert to centavos
        try {
            long wholePesos = strippedWhole.isEmpty() ? 0L : Long.parseLong(strippedWhole);
            long fractionCentavos = 0L;
            if (fractionStr.length() == 1) {
                fractionCentavos = Long.parseLong(fractionStr + "0");
            } else if (fractionStr.length() == 2) {
                fractionCentavos = Long.parseLong(fractionStr);
            }

            long centavos = wholePesos * 100L + fractionCentavos;
            if (centavos > MAX_AMOUNT_CENTAVOS) {
                return AmountResult.error(ERROR_TOO_LARGE);
            }

            return AmountResult.ok(centavos);
        } catch (NumberFormatException e) {
            return AmountResult.error(ERROR_TOO_LARGE);
        }
    }

    private static boolean containsNonZeroDigit(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '1' && c <= '9') {
                return true;
            }
        }
        return false;
    }
}
