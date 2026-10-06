# DATA VALIDATION SYSTEM
## Advanced Programming II

**Student Name:** Benjamina Mbaki  
**Student Number:** ____________________  
**College:** Gauteng City College  
**Lecturer:** Mr Owami  
**Date:** ____________________  
**GitHub Repository:** ____________________

---

# Table of Contents

1. Introduction  
2. System Design  
3. Fields  
4. Validation Checks  
5. Testing  
6. Code Walkthrough  
7. GitHub Repository  
8. Conclusion  
9. References  

---

# 1. Introduction

The Data Validation System is a Java Swing desktop application designed to capture personal information through a graphical user interface. The system validates every required field before the information is accepted.

The purpose of the system is to demonstrate practical use of data validation techniques including presence checks, type and format checks, length checks and range checks. The application also provides immediate and understandable feedback when information is invalid.

The project was developed for Advanced Programming II at Gauteng City College.

# 2. System Design

The application uses a JFrame-based graphical user interface with separate sections for the heading, form fields and action buttons.

A lavender/purple colour scheme was selected to create a consistent and modern appearance. Labels are aligned with their corresponding input fields, and validation feedback appears underneath each field.

The main workflow is:

1. Enter personal information.
2. Select a gender.
3. Leave a field or click Save to trigger validation.
4. Correct any invalid information.
5. Save the information once all fields pass validation.
6. Use Clear to reset the form or Exit to close the application.

**Figure 1: Final interface screenshot**  
[INSERT SCREENSHOT HERE]

# 3. Fields

| Field | Purpose |
|---|---|
| Name | Captures the person's first name |
| Surname | Captures the person's family name |
| ID Number | Captures a 13-digit identification number |
| Student Number | Captures the student's college/student number |
| Date of Birth | Captures the person's date of birth |
| Gender | Captures the selected gender |
| Contact Number | Captures a telephone/contact number |
| Email Address | Captures an email address |

# 4. Validation Checks

## 4.1 Name
The name cannot be empty. It must contain between 2 and 50 characters and use appropriate name characters such as letters, spaces, apostrophes or hyphens.

## 4.2 Surname
The surname has a presence check and a character/length check. This prevents an empty surname or an invalid value containing unsuitable characters.

## 4.3 ID Number
The ID number must be entered. It must contain numeric characters only and must contain exactly 13 digits.

## 4.4 Student Number
The student number must be present and must be between 4 and 20 characters. Letters, numbers and hyphens are accepted.

## 4.5 Date of Birth
The date of birth is required and must use the YYYY-MM-DD format. A future date is rejected. The calculated age must be between 5 and 120 years.

## 4.6 Gender
The user must select one of the available gender options. The form cannot be saved without a selection.

## 4.7 Contact Number
The contact number is required and must contain a valid numeric telephone format. Spaces, brackets and hyphens are tolerated and the number must contain between 10 and 15 digits after formatting.

## 4.8 Email Address
The email address is required and must follow a basic email format containing a username, @ symbol and domain.

# 5. Testing

Testing was performed by entering invalid values into each field and checking that the system displayed a clear error message.

## 5.1 Before Testing

**Figure 2: Empty name field error**  
[INSERT SCREENSHOT]

**Figure 3: Invalid surname error**  
[INSERT SCREENSHOT]

**Figure 4: Invalid ID number error**  
[INSERT SCREENSHOT]

**Figure 5: Invalid student number error**  
[INSERT SCREENSHOT]

**Figure 6: Invalid date of birth error**  
[INSERT SCREENSHOT]

**Figure 7: No gender selected error**  
[INSERT SCREENSHOT]

**Figure 8: Invalid contact number error**  
[INSERT SCREENSHOT]

**Figure 9: Invalid email address error**  
[INSERT SCREENSHOT]

## 5.2 After Testing

The same fields were then tested using valid information.

**Figure 10: Valid name and surname**  
[INSERT SCREENSHOT]

**Figure 11: Valid identification and student numbers**  
[INSERT SCREENSHOT]

**Figure 12: Valid date of birth and gender**  
[INSERT SCREENSHOT]

**Figure 13: Valid contact number and email**  
[INSERT SCREENSHOT]

## 5.3 All Input Is Valid

**Figure 14: Completed form with all fields accepted**  
[INSERT SCREENSHOT]

The final test confirmed that the Save button accepts the information only when every required field passes validation.

# 6. Code Walkthrough

## 6.1 Main Class

The `Main` class starts the application on Swing's event-dispatch thread. It also sets the system look and feel before displaying the main validation window.

```java
SwingUtilities.invokeLater(() -> {
    new ValidationFrame().setVisible(true);
});
```

## 6.2 ValidationResult

The `ValidationResult` class stores two pieces of information: whether a validation test passed and the message that should be displayed to the user.

```java
public ValidationResult(boolean valid, String message) {
    this.valid = valid;
    this.message = message;
}
```

## 6.3 Validator Class

The `Validator` class contains separate methods for each type of field. This keeps validation logic separate from the GUI and makes the program easier to maintain.

For example:

```java
public static ValidationResult validateEmail(String value) {
    if (value == null || value.trim().isEmpty())
        return new ValidationResult(false, "Email address is required.");

    if (!EMAIL_PATTERN.matcher(value.trim()).matches())
        return new ValidationResult(false, "Enter a valid email address.");

    return new ValidationResult(true, "Valid email address.");
}
```

## 6.4 Save Validation

When Save is clicked, the program validates every field. The data is accepted only if all validation results are successful.

```java
if (!validateAll()) {
    JOptionPane.showMessageDialog(
        this,
        "Please correct the highlighted fields before saving.",
        "Validation Failed",
        JOptionPane.WARNING_MESSAGE
    );
    return;
}
```

## 6.5 Object-Oriented Structure

The application is separated into logical classes:

- `Main` — starts the program.
- `ValidationFrame` — controls the graphical interface.
- `Validator` — contains validation rules.
- `ValidationResult` — stores validation outcomes.

This structure improves readability and follows object-oriented programming principles.

# 7. GitHub Repository

**Repository link:** [INSERT CLICKABLE GITHUB LINK HERE]

A marker can clone the repository and open the project in a Java IDE. The `README.md` contains instructions for running the application.

The repository should contain meaningful commits showing development progress rather than a single final upload.

Suggested commit sequence:

1. `Initial Java Swing project setup`
2. `Create validation form layout`
3. `Add validation logic`
4. `Add validation feedback`
5. `Improve GUI styling`
6. `Add README and documentation`
7. `Final testing and fixes`

# 8. Conclusion

The Data Validation System demonstrated how Java Swing can be used to create a desktop application that captures and validates user information.

The main challenge was ensuring that every field had an appropriate validation rule while also providing feedback that was easy for a normal user to understand. Separating the validation logic into its own class helped make the application more organised and easier to maintain.

The project improved my understanding of Java Swing, event handling, regular expressions, input validation, object-oriented programming and user-interface design.

# 9. References

- Oracle Java Documentation — Java Swing and Java APIs.
- Gauteng City College, Advanced Programming II Assignment Brief.
- Any additional tutorials or documentation personally consulted should be added here.
