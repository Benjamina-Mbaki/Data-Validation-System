package validation;

public final class ValidationResult {
    private final boolean valid;
    private final String message;
    private ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public static ValidationResult ok() { return new ValidationResult(true, "Looks good"); }
    public static ValidationResult fail(String message) { return new ValidationResult(false, message); }
    public boolean isValid() { return valid; }
    public String getMessage() { return message; }
}
