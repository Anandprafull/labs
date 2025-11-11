import javax.crypto.*; 
import javax.crypto.spec.SecretKeySpec;
import java.util.*; 
import java.util.Base64;

public class AESMini {
    public static void main(String[] a) {
        try (Scanner s = new Scanner(System.in)) {
            System.out.print("Enter 16-char AES key: ");
            String k = s.nextLine();
            if (k.length() != 16) { System.out.println("Key must be 16 chars!"); return; }

            SecretKeySpec key = new SecretKeySpec(k.getBytes(), "AES");
            System.out.print("Enter message: "); String m = s.nextLine();

            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, key);
            String e = Base64.getEncoder().encodeToString(c.doFinal(m.getBytes()));
            System.out.println("Encrypted: " + e);

            c.init(Cipher.DECRYPT_MODE, key);
            System.out.println("Decrypted: " + new String(c.doFinal(Base64.getDecoder().decode(e))));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
