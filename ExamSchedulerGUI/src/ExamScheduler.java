import java.io.IOException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.format.DateTimeParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExamScheduler {
    private final FileExamStorage storage;
    private List<Exam> exams;

    // Rooms and capacity
    private final List<String> rooms = Arrays.asList("N201", "N203");
    private final int totalStudents = 60;

    // Time formatter for AM/PM (e.g., 09:00 AM)
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    public ExamScheduler() {
        storage = new FileExamStorage();
    }

    public void save(List<Exam> list) throws IOException {
        storage.saveAsJson(list);
    }

    public List<Exam> load() throws IOException {
        exams = storage.loadFromJson();
        return exams;
    }

    /**
     * Assigns a room and roll numbers for the given exam while preventing time clashes.
     * Uses random preference order between available rooms.
     * On success, sets exam.room and exam.rollNumbers and exam.day and returns.
     * On failure (no room available) throws IOException with message.
     */
    public void assignRoomAndRolls(Exam exam, List<Exam> existing) throws IOException {
        if (exam == null) throw new IllegalArgumentException("Exam is null");

        // determine day from date
        try {
            LocalDate ld = LocalDate.parse(exam.getDate()); // expects YYYY-MM-DD
            String day = ld.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
            exam.setDay(day);
        } catch (DateTimeParseException ex) {
            throw new IOException("Invalid date format (expected YYYY-MM-DD): " + exam.getDate());
        }

        LocalTime start = parseTime(exam.getStartTime());
        LocalTime end = parseTime(exam.getEndTime());
        if (start == null || end == null) throw new IOException("Invalid time format. Use hh:mm AM/PM");
        if (!start.isBefore(end)) throw new IOException("Start time must be before end time.");

        // create shuffled order of rooms to give randomness
        List<String> order = new ArrayList<>(rooms);
        Collections.shuffle(order, new Random());

        // build list of existing exams to check against (use copy)
        List<Exam> others = existing == null ? new ArrayList<>() : new ArrayList<>(existing);

        for (String candidateRoom : order) {
            boolean clash = false;
            for (Exam e : others) {
                if (e.getRoom() == null) continue;
                if (!candidateRoom.equals(e.getRoom())) continue;
                // same room -> check same day
                if (exam.getDate() != null && exam.getDate().equals(e.getDate())) {
                    LocalTime s2 = parseTime(e.getStartTime());
                    LocalTime e2 = parseTime(e.getEndTime());
                    if (s2 == null || e2 == null) continue;
                    if (timesOverlap(start, end, s2, e2)) {
                        clash = true;
                        break;
                    }
                }
            }
            if (!clash) {
                // assign this room
                exam.setRoom(candidateRoom);
                // assign roll numbers based on Option A: odd->N201, even->N203
                List<Integer> rolls = new ArrayList<>();
                for (int r = 1; r <= totalStudents; r++) {
                    if ("N201".equals(candidateRoom) && (r % 2 == 1)) {
                        rolls.add(r);
                    } else if ("N203".equals(candidateRoom) && (r % 2 == 0)) {
                        rolls.add(r);
                    }
                }
                exam.setRollNumbers(rolls);
                return;
            }
        }
        throw new IOException("No available rooms for the selected time (all rooms clash).");
    }

    private boolean timesOverlap(LocalTime aStart, LocalTime aEnd, LocalTime bStart, LocalTime bEnd) {
        // overlap if startA < endB && startB < endA
        return aStart.isBefore(bEnd) && bStart.isBefore(aEnd);
    }

    private LocalTime parseTime(String t) {
        if (t == null) return null;
        try {
            return LocalTime.parse(t.toUpperCase().trim(), timeFormatter);
        } catch (Exception ex) {
            return null;
        }
    }

    public String getJsonPath() { return storage.getJsonPath(); }
}
