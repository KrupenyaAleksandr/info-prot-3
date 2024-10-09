package education.infoprotection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Optional;

public class EncryptionMachine {

    private final String ALPHABET = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ .";
    private KeyMachine keyMachine = new KeyMachine();
    private LinkedHashSet<Character> alphabet;
    private char[][] alphabetMatrix;

    public void encrypt() {
        System.out.println(keyMachine.getTextKey().get());
        System.out.println(keyMachine.getTableKey().get());
        System.out.println(getMessage().get());
        generateAlphabet(keyMachine.getTableKey().get().get("row"),
                keyMachine.getTableKey().get().get("column"));
    }

    private void generateAlphabet(int row, int column) {
        alphabet = new LinkedHashSet<>();
        alphabetMatrix = new char[row][column];

        if (keyMachine.getTextKey().isPresent()) {
            String textKey = keyMachine.getTextKey().get();

            for (char c : textKey.toCharArray()) {
                if (ALPHABET.indexOf(c) != -1) {
                    alphabet.add(c);
                }
            }

            for (char c : ALPHABET.toCharArray()) {
                alphabet.add(c);
            }

            Character[] finalAlphabet = alphabet.toArray(new Character[0]);
            for (int i = 0, k = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    alphabetMatrix[i][j] = finalAlphabet[k++];
                }
            }
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
}
