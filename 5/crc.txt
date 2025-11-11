import java.util.*;

public class crc {
    public static void main(String[] args) {
        final int POLYNOMIAL = 0x1021;  // x^16 + x^12 + x^5 + 1
        final int INITIAL_VALUE = 0xFFFF;

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter message: ");
        String message = sc.nextLine();
        byte[] data = message.getBytes();

        int crc = INITIAL_VALUE;
        for (byte b : data) {
            crc ^= (b << 8);
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0)
                    crc = (crc << 1) ^ POLYNOMIAL;
                else
                    crc <<= 1;
                crc &= 0xFFFF; 
            }
        }

        System.out.printf("Generated CRC (hex): %04X%n", crc);

        System.out.print("Enter received CRC (hex): ");
        String recvStr = sc.nextLine();
        int receivedCRC = Integer.parseInt(recvStr, 16);

        int verifyCRC = INITIAL_VALUE;
        for (byte b : data) {
            verifyCRC ^= (b << 8);
            for (int i = 0; i < 8; i++) {
                if ((verifyCRC & 0x8000) != 0)
                    verifyCRC = (verifyCRC << 1) ^ POLYNOMIAL;
                else
                    verifyCRC <<= 1;
                verifyCRC &= 0xFFFF;
            }
        }

        if (verifyCRC == receivedCRC)
            System.out.println("Data integrity verified: No error detected");
        else
            System.out.println("CRC mismatch: Data corruption detected");
    }
}
