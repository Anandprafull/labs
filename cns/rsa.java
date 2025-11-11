import java.security.*; 
import javax.crypto.Cipher; 
import java.util.Base64;

public class RSAMini {
    public static void main(String[] a) {
        try {
            KeyPairGenerator g = KeyPairGenerator.getInstance("RSA");
            g.initialize(2048); KeyPair k = g.generateKeyPair();
            Cipher c = Cipher.getInstance("RSA");

            String m = "Hello, RSA!";
            c.init(Cipher.ENCRYPT_MODE, k.getPublic());
            String e = Base64.getEncoder().encodeToString(c.doFinal(m.getBytes()));
            System.out.println("Encrypted: " + e);

            c.init(Cipher.DECRYPT_MODE, k.getPrivate());
            String d = new String(c.doFinal(Base64.getDecoder().decode(e)));
            System.out.println("Decrypted: " + d);
        } catch (Exception e) { e.printStackTrace(); }
    }
}
