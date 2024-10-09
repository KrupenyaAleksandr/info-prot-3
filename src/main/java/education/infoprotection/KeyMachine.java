package education.infoprotection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class KeyMachine {

    public Optional<HashMap<String, Integer>> getTableKey() {
        Path filepath = Paths.get("table-key-trysemus.txt");
        HashMap<String, Integer> keys = new HashMap<>();
        try (Stream<String> line = Files.lines(filepath)) {
            List<String> lines = line.toList();
            if (lines.size() == 2) {
                keys.put("row", Integer.parseInt(lines.get(0)));
                keys.put("column", Integer.parseInt(lines.get(1)));
                return Optional.of(keys);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public Optional<String> getTextKey() {
        Path filePath = Paths.get("text-key-trysemus.txt");
        try {
            if (Files.exists(filePath) && Files.size(filePath) > 0) {
                return Optional.of(Files.readString(filePath).toUpperCase());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
