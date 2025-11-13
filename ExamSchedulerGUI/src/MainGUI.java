import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class MainGUI {
    private JFrame frame;
    private ExamTableModel tableModel;
    private ExamScheduler scheduler;

    public MainGUI() {
        scheduler = new ExamScheduler();
        initUI();
    }

    private void initUI() {
        frame = new JFrame("SchedulEase");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 450);
        frame.setLayout(new BorderLayout(8,8));

        tableModel = new ExamTableModel();
        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroll = new JScrollPane(table);
        frame.add(scroll, BorderLayout.CENTER);

        JPanel leftButtons = new JPanel();
        leftButtons.setLayout(new GridLayout(0,1,6,6));
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        leftButtons.add(addBtn);
        leftButtons.add(editBtn);
        leftButtons.add(deleteBtn);
        frame.add(leftButtons, BorderLayout.WEST);

        JPanel rightButtons = new JPanel();
        rightButtons.setLayout(new GridLayout(0,1,6,6));
        JButton loadJsonBtn = new JButton("Load (JSON)");
        JButton saveBtn = new JButton("Save (JSON)");
        JButton exitBtn = new JButton("Exit");
        rightButtons.add(loadJsonBtn);
        rightButtons.add(saveBtn);
        rightButtons.add(exitBtn);
        frame.add(rightButtons, BorderLayout.EAST);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel info = new JLabel("JSON path: " + scheduler.getJsonPath());
        bottom.add(info);
        frame.add(bottom, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> {
            AddEditDialog dlg = new AddEditDialog(frame, "Add Exam", null);
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                Exam ex = dlg.getExam();
                try {
                    // assign room & rolls using existing exams in table
                    scheduler.assignRoomAndRolls(ex, tableModel.getExams());
                    tableModel.addExam(ex);
                } catch (IOException exx) {
                    JOptionPane.showMessageDialog(frame, "Cannot add exam: " + exx.getMessage());
                }
            }
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(frame, "Select a row to edit.");
                return;
            }
            Exam existing = tableModel.getExams().get(row);
            AddEditDialog dlg = new AddEditDialog(frame, "Edit Exam", existing);
            dlg.setVisible(true);
            if (dlg.isConfirmed()) {
                Exam updated = dlg.getExam();
                try {
                    // For clash checking
                    List<Exam> others = tableModel.getExams();
                    others.remove(row);
                    scheduler.assignRoomAndRolls(updated, others);
                    tableModel.updateExam(row, updated);
                } catch (IOException exx) {
                    JOptionPane.showMessageDialog(frame, "Cannot update exam: " + exx.getMessage());
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(frame, "Select a row to delete.");
                return;
            }
            int r = JOptionPane.showConfirmDialog(frame, "Delete selected exam?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                tableModel.removeExam(row);
            }
        });

        saveBtn.addActionListener(e -> {
            try {
                scheduler.save(tableModel.getExams());
                JOptionPane.showMessageDialog(frame, "Saved to " + scheduler.getJsonPath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving: " + ex.getMessage());
            }
        });

        loadJsonBtn.addActionListener(e -> {
            try {
                List<Exam> loaded = scheduler.load();
                tableModel.setExams(loaded);
                JOptionPane.showMessageDialog(frame, "Loaded " + loaded.size() + " exams from " + scheduler.getJsonPath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error loading: " + ex.getMessage());
            }
        });

        exitBtn.addActionListener(e -> System.exit(0));

        // show
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}
