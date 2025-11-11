import java.util.*;

public class CaesarCipher {
    static String cipher(String txt, int s) {
        StringBuilder r = new StringBuilder();
        for (char c : txt.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                r.append((char) ((c - base + s) % 26 + base));
            } else r.append(c);
        }
        return r.toString();
    }
    public static void main(String[] a) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter text: "); String t = sc.nextLine();
        System.out.print("Shift: "); int s = sc.nextInt();
        String e = cipher(t, s), d = cipher(e, 26 - s);
        System.out.println("Encrypted: " + e + "\nDecrypted: " + d);
    }
}
