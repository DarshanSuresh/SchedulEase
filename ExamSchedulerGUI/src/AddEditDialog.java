import javax.swing.*;
import java.awt.*;

public class AddEditDialog extends JDialog {
    private boolean confirmed = false;
    private final JTextField subjectField = new JTextField(20);
    private final JTextField dateField = new JTextField(12);      // YYYY-MM-DD
    private final JTextField startField = new JTextField(12);     // 09:00 AM
    private final JTextField endField = new JTextField(12);       // 12:30 PM

    public AddEditDialog(Frame owner, String title, Exam existing) {
        super(owner, title, true);
        setLayout(new BorderLayout(8,8));

        JPanel form = new JPanel(new GridLayout(4,2,6,6));
        form.add(new JLabel("Subject:"));
        form.add(subjectField);
        form.add(new JLabel("Date (YYYY-MM-DD):"));
        form.add(dateField);
        form.add(new JLabel("Start Time (hh:mm AM/PM):"));
        form.add(startField);
        form.add(new JLabel("End Time (hh:mm AM/PM):"));
        form.add(endField);
        add(form, BorderLayout.CENTER);

        if (existing != null) {
            subjectField.setText(existing.getSubject());
            dateField.setText(existing.getDate());
            startField.setText(existing.getStartTime());
            endField.setText(existing.getEndTime());
        }

        JPanel buttons = new JPanel();
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        buttons.add(ok);
        buttons.add(cancel);
        add(buttons, BorderLayout.SOUTH);

        ok.addActionListener(e -> {
            if (subjectField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Subject cannot be empty");
                return;
            }
            if (dateField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Date cannot be empty");
                return;
            }
            if (startField.getText().trim().isEmpty() || endField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Start and End times cannot be empty");
                return;
            }
            confirmed = true;
            setVisible(false);
        });

        cancel.addActionListener(e -> {
            confirmed = false;
            setVisible(false);
        });

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isConfirmed() { return confirmed; }

    public Exam getExam() {
        return new Exam(subjectField.getText().trim(),
                        dateField.getText().trim(),
                        startField.getText().trim(),
                        endField.getText().trim());
    }
}
