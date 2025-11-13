import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExamTableModel extends AbstractTableModel {
    private final String[] cols = {"Subject", "Date", "Day", "Start Time", "End Time", "Room", "Rolls"};
    private final List<Exam> exams = new ArrayList<>();

    public void setExams(List<Exam> list) {
        exams.clear();
        if (list != null) exams.addAll(list);
        fireTableDataChanged();
    }

    public List<Exam> getExams() {
        return new ArrayList<>(exams);
    }

    public void addExam(Exam e) {
        exams.add(e);
        fireTableRowsInserted(exams.size()-1, exams.size()-1);
    }

    public void updateExam(int index, Exam e) {
        if (index >= 0 && index < exams.size()) {
            exams.set(index, e);
            fireTableRowsUpdated(index, index);
        }
    }

    public void removeExam(int index) {
        if (index >= 0 && index < exams.size()) {
            exams.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }

    @Override
    public int getRowCount() { return exams.size(); }

    @Override
    public int getColumnCount() { return cols.length; }

    @Override
    public String getColumnName(int column) { return cols[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Exam e = exams.get(rowIndex);
        switch (columnIndex) {
            case 0: return e.getSubject();
            case 1: return e.getDate();
            case 2: return e.getDay();
            case 3: return e.getStartTime();
            case 4: return e.getEndTime();
            case 5: return e.getRoom();
            case 6:
                return e.getRollNumbers().stream().map(Object::toString).collect(Collectors.joining(", "));
            default: return "";
        }
    }
}
