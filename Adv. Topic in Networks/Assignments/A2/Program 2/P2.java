import java.util.Scanner;

public class P2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String inputHexValue = sc.nextLine();

        inputHexValue = inputHexValue.toUpperCase();

        System.out.println();

        System.out.println("Input: " + inputHexValue);

        BitStuffing bitStuffing = new BitStuffing();

        String binaryValue = bitStuffing.HexToBinary(inputHexValue);
        System.out.println("Conversion to binary: " + binaryValue);

        String stuffedValue = bitStuffing.stuffing(binaryValue);
        System.out.println("After bit stuffing: " + stuffedValue);

        String unstuffedValue = bitStuffing.unStuffing(stuffedValue);
        System.out.println("After bit unstuffing: " + unstuffedValue);

        String hexValue = bitStuffing.BinaryToHex(unstuffedValue);

        System.out.println("Output: " + hexValue);
    }
}