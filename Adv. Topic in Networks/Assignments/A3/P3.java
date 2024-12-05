import java.util.Random;

public class P3 {

    public static String generateMessage(int sizeInBytes) {
        StringBuilder message = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < sizeInBytes * 8; i++) {
            int bit = random.nextInt(2);
            message.append(bit);
        }

        return message.toString();
    }


    public static String generateBurstError(int length) {
        StringBuilder binaryString = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < length; i++) {
            int bit = random.nextInt(2);
            binaryString.append(bit);
        }
        
        return binaryString.toString();
    }

    public static int generateRandomNumber(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    public static String insertBurstError(String message, String error){
        int index = generateRandomNumber(message.length() - error.length());
        return message.substring(0, index) + error + message.substring(index+error.length(), message.length());
    }

    public static void main(String[] args) {

        System.out.printf("%s\t\t%s\t\t\t%s%n", "Experiment No.", "Burst error length", "Error detected?");
        
        int busrtErrorSize = 32;

        for(int i=0;i<50;i++){
            String message = generateMessage(1520);
            String standardCRCGenerator = "100000100110000010001110110110111";
            CRC crc = new CRC();
            String transmittedMessage = crc.generateCRC(message, standardCRCGenerator);
            String error = generateBurstError(busrtErrorSize);
            transmittedMessage = insertBurstError(transmittedMessage, error);
            boolean errorDetected = !crc.checkMessage(transmittedMessage, standardCRCGenerator);
            System.out.printf("\t%d\t\t\t%d\t\t\t\t%s%n", i+1, busrtErrorSize, errorDetected ? "Yes" : "No");
            busrtErrorSize++;
        }

    }
}
