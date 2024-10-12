package education.infoprotection;

public class Main {
    public static void main(String[] args) {
        EncryptionMachine encryptionMachine = new EncryptionMachine();
        CryptoAnalysisMachine cryptoAnalysisMachine = new CryptoAnalysisMachine();
        String shuffledKey = encryptionMachine.encrypt();
        cryptoAnalysisMachine.cryptoAnalyse(shuffledKey);
    }
}