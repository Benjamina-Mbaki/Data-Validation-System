package ui;

import validation.ValidationResult;
import validation.Validators;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.function.Supplier;


public class MainForm extends JFrame {
    private final JTextField nameField = new JTextField(20);
    private final JTextField surnameField = new JTextField(20);
    private final JTextField idField = new JTextField(20);
    private final JTextField dobField = new JTextField(20);
    private final JTextField phoneField = new JTextField(20);
    private final JTextField emailField = new JTextField(20);
    private final JRadioButton male = new JRadioButton("Male");
    private final JRadioButton female = new JRadioButton("Female");
    private final JRadioButton other = new JRadioButton("Other");
    private final ButtonGroup genderGroup = new ButtonGroup();
    private final JLabel nameMsg = feedbackLabel();
    private final JLabel surnameMsg = feedbackLabel();
    private final JLabel idMsg = feedbackLabel();
    private final JLabel dobMsg = feedbackLabel();
    private final JLabel genderMsg = feedbackLabel();
    private final JLabel phoneMsg = feedbackLabel();
    private final JLabel emailMsg = feedbackLabel();
    private final JButton saveButton = new JButton("Save");

    public MainForm() {
        super("Student Data Validation System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Theme.BACKGROUND);
        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.CENTER);
        add(buildButtons(), BorderLayout.SOUTH);
        wireValidation();
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

   

    private JPanel buildHeader() {
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(Theme.NAVY);
        header.setBorder(new EmptyBorder(16, 24, 16, 24));
        JLabel title = new JLabel("Personal Details Form");
        title.setFont(Theme.TITLE);
        title.setForeground(Color.WHITE);
        JLabel sub = new JLabel("Fill in every field. Each one is checked as you type.");
        sub.setFont(Theme.HINT);
        sub.setForeground(Theme.ORANGE);
        header.add(title);
        header.add(sub);
        return header;
    }

    private JPanel buildForm() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.CARD);
        card.setBorder(new CompoundBorder(new EmptyBorder(16, 16, 8, 16),
                new CompoundBorder(new LineBorder(Theme.BORDER), new EmptyBorder(16, 20, 16, 20))));

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        genderPanel.setBackground(Theme.CARD);
        for (JRadioButton rb : new JRadioButton[]{male, female, other}) {
            rb.setFont(Theme.INPUT);
            rb.setBackground(Theme.CARD);
            genderGroup.add(rb);
            genderPanel.add(rb);
        }

        int row = 0;
        addRow(card, row++, "Name", nameField, nameMsg);
        addRow(card, row++, "Surname", surnameField, surnameMsg);
        addRow(card, row++, "ID number (13 digits)", idField, idMsg);
        addRow(card, row++, "Date of birth (yyyy-mm-dd)", dobField, dobMsg);
        addRow(card, row++, "Gender", genderPanel, genderMsg);
        addRow(card, row++, "Contact number", phoneField, phoneMsg);
        addRow(card, row, "Email address", emailField, emailMsg);
        return card;
    }

    private void addRow(JPanel p, int row, String text, JComponent input, JLabel msg) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.LABEL);
        label.setForeground(Theme.NAVY);
        if (input instanceof JTextField) {
            input.setFont(Theme.INPUT);
            input.setBorder(inputBorder(Theme.BORDER));
        }
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = row * 2; c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(8, 0, 0, 16);
        p.add(label, c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1; c.insets = new Insets(8, 0, 0, 0);
        p.add(input, c);
        c.gridy = row * 2 + 1; c.insets = new Insets(2, 0, 0, 0);
        p.add(msg, c);
    }

    private JPanel buildButtons() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bar.setBackground(Theme.BACKGROUND);
        bar.setBorder(new EmptyBorder(0, 16, 8, 16));
        JButton clear = styledButton("Clear", Theme.MUTED);
        JButton exit = styledButton("Exit", Theme.NAVY);
        styleButton(saveButton, Theme.VALID);
        saveButton.addActionListener(e -> save());
        clear.addActionListener(e -> clearForm());
        exit.addActionListener(e -> System.exit(0));
        bar.add(clear);
        bar.add(exit);
        bar.add(saveButton);
        return bar;
    }

    private void wireValidation() {
        live(nameField, nameMsg, () -> Validators.name(nameField.getText(), "Name"));
        live(surnameField, surnameMsg, () -> Validators.name(surnameField.getText(), "Surname"));
        live(idField, idMsg, () -> Validators.idNumber(idField.getText()));
        live(dobField, dobMsg, () -> Validators.dateOfBirth(dobField.getText()));
        live(phoneField, phoneMsg, () -> Validators.contactNumber(phoneField.getText()));
        live(emailField, emailMsg, () -> Validators.email(emailField.getText()));
        for (JRadioButton rb : new JRadioButton[]{male, female, other}) {
            rb.addActionListener(e -> show(genderMsg, null, Validators.gender(selectedGender())));
        }
    }

    private void live(JTextField field, JLabel msg, Supplier<ValidationResult> rule) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { show(msg, field, rule.get()); }
            public void removeUpdate(DocumentEvent e) { show(msg, field, rule.get()); }
            public void changedUpdate(DocumentEvent e) { show(msg, field, rule.get()); }
        });
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusLost(java.awt.event.FocusEvent e) { show(msg, field, rule.get()); }
        });
    }

    private void show(JLabel msg, JTextField field, ValidationResult r) {
        msg.setText((r.isValid() ? "\u2714 " : "\u2716 ") + r.getMessage());
        msg.setForeground(r.isValid() ? Theme.VALID : Theme.INVALID);
        if (field != null) field.setBorder(inputBorder(r.isValid() ? Theme.VALID : Theme.INVALID));
    }

    private boolean validateAll() {
        ValidationResult[] results = {
                Validators.name(nameField.getText(), "Name"),
                Validators.name(surnameField.getText(), "Surname"),
                Validators.idNumber(idField.getText()),
                Validators.dateOfBirth(dobField.getText()),
                Validators.gender(selectedGender()),
                Validators.contactNumber(phoneField.getText()),
                Validators.email(emailField.getText())
        };
        show(nameMsg, nameField, results[0]);
        show(surnameMsg, surnameField, results[1]);
        show(idMsg, idField, results[2]);
        show(dobMsg, dobField, results[3]);
        show(genderMsg, null, results[4]);
        show(phoneMsg, phoneField, results[5]);
        show(emailMsg, emailField, results[6]);
        for (ValidationResult r : results) if (!r.isValid()) return false;
        return true;
    }

    private void save() {
        if (!validateAll()) {
            JOptionPane.showMessageDialog(this, "Some fields need attention. Please fix the items marked in red.",
                    "Cannot save yet", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Path file = Paths.get("records.csv");
            boolean isNew = !Files.exists(file);
            try (PrintWriter out = new PrintWriter(new FileWriter(file.toFile(), true))) {
                if (isNew) out.println("saved_at,name,surname,id,dob,gender,phone,email");
                out.println(String.join(",", LocalDateTime.now().toString(), nameField.getText().trim(),
                        surnameField.getText().trim(), idField.getText().trim(), dobField.getText().trim(),
                        selectedGender(), phoneField.getText().replace(" ", ""), emailField.getText().trim()));
            }
            JOptionPane.showMessageDialog(this, "All details are valid and have been saved to records.csv.",
                    "Saved", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Sorry, the file could not be saved. Please try again.",
                    "Save failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        for (JTextField f : new JTextField[]{nameField, surnameField, idField, dobField, phoneField, emailField}) {
            f.setText("");
            f.setBorder(inputBorder(Theme.BORDER));
        }
        genderGroup.clearSelection();
        for (JLabel l : new JLabel[]{nameMsg, surnameMsg, idMsg, dobMsg, genderMsg, phoneMsg, emailMsg}) l.setText(" ");
        nameField.requestFocusInWindow();
    }

    private String selectedGender() {
        if (male.isSelected()) return "Male";
        if (female.isSelected()) return "Female";
        if (other.isSelected()) return "Other";
        return "";
    }

    private static JLabel feedbackLabel() {
        JLabel l = new JLabel(" ");
        l.setFont(Theme.HINT);
        return l;
    }

    private static CompoundBorder inputBorder(Color c) {
        return new CompoundBorder(new LineBorder(c, 1), new EmptyBorder(6, 8, 6, 8));
    }

    private static JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        styleButton(b, bg);
        return b;
    }

    private static void styleButton(JButton b, Color bg) {
        b.setFont(Theme.LABEL);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(new EmptyBorder(9, 22, 9, 22));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
