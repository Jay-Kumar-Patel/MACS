import java.util.Scanner;

public class P2{

    public static void main(String[] args) {

        CRC crc = new CRC();

        Scanner scanner = new Scanner(System.in);

        System.out.print("G(x): ");
        String generator = scanner.nextLine();

        System.out.print("M(x): ");
        String message = scanner.nextLine();

        String transmittedMessage = crc.generateCRC(message, generator);
        System.out.println("P(x): " + transmittedMessage);

        System.out.print("Enter the received message: ");
        String receivedMessage = scanner.nextLine();

        System.out.print("G(x): ");
        String generatorAtReceiver = scanner.nextLine();

        if (crc.checkMessage(receivedMessage, generatorAtReceiver)) {
            System.out.println("The received message is error-free.");
        } else {
            System.out.println("The received message has errors.");
        }

        scanner.close();
    }
}