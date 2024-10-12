package education.infoprotection;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class EncryptionMachine {

    private final String ALPHABET = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ .";
    private KeyMachine keyMachine = new KeyMachine();
    private String alphabet = "";

    public String encrypt() {
        String message = getMessage().get();
        int columnCount = keyMachine.getTableKey().get().get("column");
        generateAlphabet();

        StringBuilder encryptedMessage = new StringBuilder();
        char[] messageArray = message.toUpperCase().toCharArray();
        char[] alphabetArray = alphabet.toCharArray();

        for (int i = 0; i < message.length(); i++) {
            encryptedMessage.append(alphabetArray[(alphabet.indexOf(messageArray[i]) +
                    columnCount) % alphabet.length()]);
        }

        printToFile(encryptedMessage.toString());
        return shuffleKey(alphabet.substring(0, 6));
    }

    private String shuffleKey(String key) {
        List<String> keyToShuffle = Arrays.asList(key.split(""));
        Collections.shuffle(keyToShuffle);
        StringBuilder stringBuilder = new StringBuilder();
        keyToShuffle.forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    private void generateAlphabet() {
        LinkedHashSet<Character> alphabetHashSet = new LinkedHashSet<>();

        if (keyMachine.getTextKey().isPresent()) {
            String textKey = keyMachine.getTextKey().get();

            for (char c : textKey.toCharArray()) {
                if (ALPHABET.indexOf(c) != -1) {
                    alphabetHashSet.add(c);
                }
            }

            for (char c : ALPHABET.toCharArray()) {
                alphabetHashSet.add(c);
            }

            StringBuilder alphabetStringBuilder = new StringBuilder();
            alphabetHashSet.forEach(alphabetStringBuilder::append);
            alphabet = alphabetStringBuilder.toString();
        }
    }

    private Optional<String> getMessage() {
        Path filepath = Paths.get("text-trysemus.txt");
        try {
            return Optional.of(Files.readString(filepath).toUpperCase());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void printToFile(String encryptedMessage) {
        String filePath = "encrypted-text-trysemus.txt";

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(encryptedMessage);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
