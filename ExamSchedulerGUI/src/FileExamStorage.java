import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileExamStorage {
    private static final String DATA_DIR = "data";
    private static final String JSON_FILE = DATA_DIR + File.separator + "exams.json";

    public FileExamStorage() {
        ensureDataDir();
    }

    private void ensureDataDir() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    // Save as JSON (array of exam objects)
    public void saveAsJson(List<Exam> exams) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(JSON_FILE))) {
            bw.write("[");
            bw.newLine();
            for (int i = 0; i < exams.size(); i++) {
                Exam e = exams.get(i);
                bw.write("  {");
                bw.newLine();
                bw.write("    \"subject\": " + toJsonString(e.getSubject()) + ",");
                bw.newLine();
                bw.write("    \"date\": " + toJsonString(e.getDate()) + ",");
                bw.newLine();
                bw.write("    \"day\": " + toJsonString(e.getDay()) + ",");
                bw.newLine();
                bw.write("    \"startTime\": " + toJsonString(e.getStartTime()) + ",");
                bw.newLine();
                bw.write("    \"endTime\": " + toJsonString(e.getEndTime()) + ",");
                bw.newLine();
                bw.write("    \"room\": " + toJsonString(e.getRoom()) + ",");
                bw.newLine();
                bw.write("    \"rollNumbers\": [");
                for (int j = 0; j < e.getRollNumbers().size(); j++) {
                    bw.write(String.valueOf(e.getRollNumbers().get(j)));
                    if (j < e.getRollNumbers().size() - 1) bw.write(", ");
                }
                bw.write("]");
                bw.newLine();
                bw.write("  }" + (i < exams.size() - 1 ? "," : ""));
                bw.newLine();
            }
            bw.write("]");
            bw.newLine();
        }
    }

    // Load from JSON (simple parser for the format we write)
    public List<Exam> loadFromJson() throws IOException {
        List<Exam> list = new ArrayList<>();
        File f = new File(JSON_FILE);
        if (!f.exists()) return list;
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(JSON_FILE))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
        }
        String json = sb.toString();
        // split objects roughly
        String[] parts = json.split("\\{");
        for (String part : parts) {
            if (part.contains("\"subject\"")) {
                String subj = extractJsonValue(part, "subject");
                String date = extractJsonValue(part, "date");
                String day = extractJsonValue(part, "day");
                String start = extractJsonValue(part, "startTime");
                String end = extractJsonValue(part, "endTime");
                String room = extractJsonValue(part, "room");
                List<Integer> rolls = extractJsonArray(part, "rollNumbers");
                Exam e = new Exam(subj, date, start, end);
                e.setDay(day);
                e.setRoom(room);
                e.setRollNumbers(rolls);
                list.add(e);
            }
        }
        return list;
    }

    private static String extractJsonValue(String block, String key) {
        int idx = block.indexOf("\"" + key + "\"");
        if (idx < 0) return "";
        int colon = block.indexOf(":", idx);
        if (colon < 0) return "";
        int start = block.indexOf("\"", colon);
        if (start < 0) return "";
        int end = block.indexOf("\"", start + 1);
        if (end < 0) return "";
        return unescapeJson(block.substring(start + 1, end));
    }

    private static List<Integer> extractJsonArray(String block, String key) {
        List<Integer> res = new ArrayList<>();
        int idx = block.indexOf("\"" + key + "\"");
        if (idx < 0) return res;
        int colon = block.indexOf(":", idx);
        if (colon < 0) return res;
        int start = block.indexOf("[", colon);
        int end = block.indexOf("]", start);
        if (start < 0 || end < 0) return res;
        String inner = block.substring(start + 1, end).trim();
        if (inner.isEmpty()) return res;
        String[] nums = inner.split(",");
        for (String n : nums) {
            try {
                res.add(Integer.parseInt(n.trim()));
            } catch (NumberFormatException ignored) {}
        }
        return res;
    }

    private static String toJsonString(String s) {
        if (s == null) s = "";
        return "\"" + escapeJson(s) + "\"";
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String unescapeJson(String s) {
        return s.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }

    public String getJsonPath() { return JSON_FILE; }
}
