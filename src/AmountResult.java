/**
 * Represents the result of validating a monetary amount string.
 * Holds either a valid centavos amount or a human-readable validation error message.
 */
public final class AmountResult {
    private final long centavos;
    private final String errorMessage;
    private final boolean valid;

    private AmountResult(long centavos, String errorMessage, boolean valid) {
        this.centavos = centavos;
        this.errorMessage = errorMessage;
        this.valid = valid;
    }

    public static AmountResult ok(long centavos) {
        return new AmountResult(centavos, null, true);
    }

    public static AmountResult error(String message) {
        return new AmountResult(0L, message, false);
    }

    public boolean isValid() {
        return valid;
    }

    public long getCentavos() {
        return centavos;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public long centavos() {
        return centavos;
    }

    public String errorMessage() {
        return errorMessage;
    }
}
