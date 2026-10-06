package validation;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public final class Validators {
    private static final Pattern NAME_CHARS = Pattern.compile("[\\p{L} '\\-]+");
    private static final Pattern DIGITS = Pattern.compile("\\d+");
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_CHARS = Pattern.compile("\\+?\\d+");

    public static final int MIN_AGE = 16;
    public static final int MAX_AGE = 100;

    private Validators() { }

    public static ValidationResult name(String input, String fieldName) {
        String v = input.trim();
        if (v.isEmpty()) return ValidationResult.fail(fieldName + " cannot be empty.");
        if (!NAME_CHARS.matcher(v).matches())
            return ValidationResult.fail(fieldName + " may only contain letters, spaces, hyphens or apostrophes.");
        if (v.length() < 2) return ValidationResult.fail(fieldName + " must be at least 2 characters.");
        if (v.length() > 50) return ValidationResult.fail(fieldName + " must be 50 characters or fewer.");
        return ValidationResult.ok();
    }

    public static ValidationResult idNumber(String input) {
        String v = input.trim();
        if (v.isEmpty()) return ValidationResult.fail("ID number cannot be empty.");
        if (!DIGITS.matcher(v).matches()) return ValidationResult.fail("ID number must contain digits only.");
        if (v.length() != 13)
            return ValidationResult.fail("ID number must be exactly 13 digits (you typed " + v.length() + ").");
        try {
            int yy = Integer.parseInt(v.substring(0, 2));
            int mm = Integer.parseInt(v.substring(2, 4));
            int dd = Integer.parseInt(v.substring(4, 6));
            LocalDate.of(2000 + yy, mm, dd); // throws if month/day impossible
        } catch (DateTimeException e) {
            return ValidationResult.fail("The first 6 digits must be a real date of birth (YYMMDD).");
        }
        if (!luhnValid(v)) return ValidationResult.fail("This ID number's check digit is incorrect. Please re-check it.");
        return ValidationResult.ok();
    }

    public static ValidationResult dateOfBirth(String input) {
        String v = input.trim();
        if (v.isEmpty()) return ValidationResult.fail("Date of birth cannot be empty.");
        if (!v.matches("\\d{4}-\\d{2}-\\d{2}"))
            return ValidationResult.fail("Use the format yyyy-mm-dd, e.g. 2001-05-23.");
        LocalDate dob;
        try {
            dob = LocalDate.parse(v);
        } catch (DateTimeException e) {
            return ValidationResult.fail("That date does not exist. Please check the day and month.");
        }
        if (dob.isAfter(LocalDate.now())) return ValidationResult.fail("Date of birth cannot be in the future.");
        int age = Period.between(dob, LocalDate.now()).getYears();
        if (age < MIN_AGE) return ValidationResult.fail("You must be at least " + MIN_AGE + " years old.");
        if (age > MAX_AGE) return ValidationResult.fail("Age must be " + MAX_AGE + " or less. Please check the year.");
        return ValidationResult.ok();
    }

    public static ValidationResult gender(String selected) {
        if (selected == null || selected.isEmpty()) return ValidationResult.fail("Please select a gender option.");
        return ValidationResult.ok();
    }

    public static ValidationResult contactNumber(String input) {
        String v = input.replace(" ", "");
        if (v.isEmpty()) return ValidationResult.fail("Contact number cannot be empty.");
        if (!PHONE_CHARS.matcher(v).matches())
            return ValidationResult.fail("Contact number may only contain digits (and a leading +).");
        int expected = v.startsWith("+") ? 12 : 10;
        if (v.length() != expected)
            return ValidationResult.fail("Number must be " + expected + " characters (e.g. 0821234567 or +27821234567).");
        if (!(v.startsWith("0") || v.startsWith("+27")))
            return ValidationResult.fail("Number must start with 0 or +27.");
        return ValidationResult.ok();
    }

    public static ValidationResult email(String input) {
        String v = input.trim();
        if (v.isEmpty()) return ValidationResult.fail("Email address cannot be empty.");
        if (v.length() > 100) return ValidationResult.fail("Email must be 100 characters or fewer.");
        if (!EMAIL.matcher(v).matches())
            return ValidationResult.fail("Enter a valid email, e.g. name@example.com.");
        return ValidationResult.ok();
    }

    private static boolean luhnValid(String digits) {
        int sum = 0;
        boolean dbl = false;
        for (int i = digits.length() - 1; i >= 0; i--) {
            int d = digits.charAt(i) - '0';
            if (dbl) { d *= 2; if (d > 9) d -= 9; }
            sum += d;
            dbl = !dbl;
        }
        return sum % 10 == 0;
    }
}
