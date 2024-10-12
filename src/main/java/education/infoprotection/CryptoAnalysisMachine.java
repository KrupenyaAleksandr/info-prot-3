package education.infoprotection;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CryptoAnalysisMachine {

    KeyMachine keyMachine = new KeyMachine();
    private final String ALPHABET = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ .";
    private HashMap<String, Double> statistics;
    private String encryptedMessage;
    List<String> keyVariantsList = new ArrayList<>();
    TreeSet<Divergence> divergenceTreeSet = new TreeSet<>();

    public void cryptoAnalyse(String revealedKey) {
        int keySize = revealedKey.length();
        encryptedMessage = getEncryptedMessage().get();
        boolean[] used = new boolean[keySize];
        generateAllCombinations(revealedKey.toCharArray(), "", keySize, used);
        initStats();
        cryptoAnalyse();
    }

    private void cryptoAnalyse() {
        int columnCount = keyMachine.getTableKey().get().get("column");
        for (String key : keyVariantsList) {
            HashMap<String, Double> decryptedStatistics =
                    processStatistics(decrypt(encryptedMessage, generateAlphabet(key), columnCount));
            countDivergence(decryptedStatistics, key);
        }
        printToFile(decrypt(encryptedMessage, generateAlphabet(divergenceTreeSet.first().getKey()), columnCount));
        System.out.println(divergenceTreeSet.first().getKey());
    }

    private void countDivergence(HashMap<String, Double> decryptedStatistics, String key) {
        Double divergence = 0.0;
        String charStr;

        for (char c : ALPHABET.toCharArray()) {
            charStr = String.valueOf(c).toUpperCase();
            if (decryptedStatistics.get(charStr) <= 0) {
                continue;
            }
            divergence += Math.pow(decryptedStatistics.get(charStr) - statistics.get(charStr), 2);
        }

        divergenceTreeSet.add(new Divergence(key, divergence));
    }

    private HashMap<String, Double> processStatistics(String decryptedMessage) {
        HashMap<String, Integer> charCount = new HashMap<>();

        for (char c : decryptedMessage.toCharArray()) {
            String charString = String.valueOf(c);
            if (charCount.containsKey(charString)) {
                charCount.put(charString, charCount.get(charString) + 1);
            }
            else {
                charCount.put(String.valueOf(c), 1);
            }
        }

        HashMap<String, Double> decryptedStatistics = new HashMap<>();
        String charStr;
        int decryptedMessageSize = decryptedMessage.length();

        for (char c : ALPHABET.toCharArray()) {
            charStr = String.valueOf(c).toUpperCase();
                decryptedStatistics.put(charStr, Double.valueOf(charCount.get(charStr)) / decryptedMessageSize);
        }

        return decryptedStatistics;
    }

    private String decrypt(String encryptedMessage, String alphabetVariant, int columnCount) {
        StringBuilder decryptedMessage = new StringBuilder();
        char[] messageArray = encryptedMessage.toCharArray();
        char[] alphabetArray = alphabetVariant.toCharArray();

        for (int i = 0; i < encryptedMessage.length(); i++) {
            int charIndex = alphabetVariant.indexOf(messageArray[i]) - columnCount;
            if (charIndex < 0) {
                decryptedMessage.append(alphabetArray[charIndex + alphabetVariant.length()]);
            }
            else {
                decryptedMessage.append(alphabetArray[charIndex]);
            }
        }

        return decryptedMessage.toString();
    }

    private String generateAlphabet(String keyVariant) {
        LinkedHashSet<Character> alphabetHashSet = new LinkedHashSet<>();

        for (char c : keyVariant.toCharArray()) {
            if (ALPHABET.indexOf(c) != -1) {
                alphabetHashSet.add(c);
            }
        }

        for (char c : ALPHABET.toCharArray()) {
            alphabetHashSet.add(c);
        }

        StringBuilder alphabetStringBuilder = new StringBuilder();
        alphabetHashSet.forEach(alphabetStringBuilder::append);
        return alphabetStringBuilder.toString();
    }

    private void generateAllCombinations(char[] chars, String keyVar, int length, boolean[] used) {
        if (length == 0) {
            keyVariantsList.add(keyVar);
            return;
        }

        for (int i = 0; i < chars.length; i++) {
            if (!used[i]) {
                used[i] = true;
                generateAllCombinations(chars, keyVar + chars[i], length - 1, used);
                used[i] = false;
            }
        }
    }

    private Optional<String> getEncryptedMessage() {
        Path filepath = Paths.get("encrypted-text-trysemus.txt");
        try {
            return Optional.of(Files.readString(filepath).toUpperCase());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void printToFile(String decryptedMessage) {
        String filePath = "decrypted-text-trysemus.txt";

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(decryptedMessage);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void initStats() {
        statistics = new HashMap<>();
        statistics.put(" ", 0.175);
        statistics.put(".", 0.100);
        statistics.put("О", 0.090);
        statistics.put("Е", 0.072);
        statistics.put("Ё", 0.072);
        statistics.put("А", 0.062);
        statistics.put("И", 0.062);
        statistics.put("Н", 0.053);
        statistics.put("Т", 0.053);
        statistics.put("С", 0.045);
        statistics.put("Р", 0.040);
        statistics.put("В", 0.038);
        statistics.put("Л", 0.035);
        statistics.put("К", 0.028);
        statistics.put("М", 0.026);
        statistics.put("Д", 0.025);
        statistics.put("П", 0.023);
        statistics.put("У", 0.021);
        statistics.put("Я", 0.018);
        statistics.put("Ы", 0.016);
        statistics.put("З", 0.016);
        statistics.put("Ь", 0.014);
        statistics.put("Ъ", 0.014);
        statistics.put("Б", 0.014);
        statistics.put("Г", 0.013);
        statistics.put("Ч", 0.012);
        statistics.put("Й", 0.010);
        statistics.put("Х", 0.009);
        statistics.put("Ж", 0.007);
        statistics.put("Ю", 0.006);
        statistics.put("Ш", 0.006);
        statistics.put("Ц", 0.006);
        statistics.put("Щ", 0.003);
        statistics.put("Э", 0.003);
        statistics.put("Ф", 0.002);
    }
}
