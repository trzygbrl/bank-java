/**
 * Represents the result of validating a monetary amount string.
 * Holds either a valid centavos amount or a human-readable validation error message.
 */
public final class AmountResult {
    private final long centavos;
    private final String errorMessage;
    private final boolean valid;

    /**
     * Private constructor to create an AmountResult.
     *
     * @param centavos the validated amount in centavos (0 if invalid)
     * @param errorMessage the error message (null if valid)
     * @param valid true if validation succeeded, false otherwise
     */
    private AmountResult(long centavos, String errorMessage, boolean valid) {
        this.centavos = centavos;
        this.errorMessage = errorMessage;
        this.valid = valid;
    }

    /**
     * Factory method creating a successful validation result.
     *
     * @param centavos the validated monetary amount in centavos
     * @return a successful AmountResult instance
     */
    public static AmountResult ok(long centavos) {
        return new AmountResult(centavos, null, true);
    }

    /**
     * Factory method creating a failed validation result.
     *
     * @param message the validation error message
     * @return a failed AmountResult instance
     */
    public static AmountResult error(String message) {
        return new AmountResult(0L, message, false);
    }

    /**
     * Checks if the validation was successful.
     *
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Returns the validated amount in centavos.
     *
     * @return amount in centavos (0 if invalid)
     */
    public long getCentavos() {
        return centavos;
    }

    /**
     * Returns the validation error message.
     *
     * @return error message string (null if valid)
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
