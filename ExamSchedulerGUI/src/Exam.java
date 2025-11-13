import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Exam {
    private String subject;
    private String date;      
    private String day;    
    private String startTime; 
    private String endTime;   
    private String room;      
    private List<Integer> rollNumbers = new ArrayList<>();

    public Exam() {}

    public Exam(String subject, String date, String startTime, String endTime) {
        this.subject = subject;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public List<Integer> getRollNumbers() { return new ArrayList<>(rollNumbers); }
    public void setRollNumbers(List<Integer> rollNumbers) {
        this.rollNumbers = new ArrayList<>();
        if (rollNumbers != null) this.rollNumbers.addAll(rollNumbers);
    }

    @Override
    public String toString() {
        return subject + "," + date + "," + startTime + "," + endTime + "," + room + "," + rollNumbers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exam)) return false;
        Exam exam = (Exam) o;
        return Objects.equals(subject, exam.subject) &&
                Objects.equals(date, exam.date) &&
                Objects.equals(startTime, exam.startTime) &&
                Objects.equals(endTime, exam.endTime) &&
                Objects.equals(room, exam.room) &&
                Objects.equals(rollNumbers, exam.rollNumbers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, date, startTime, endTime, room, rollNumbers);
    }
}
