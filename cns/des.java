import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.*;
import java.security.*;
import java.util.Base64;

public class DESDemo {
    static SecretKey keyFromPassword(String pass) throws Exception {
        byte[] k = new byte[8], p = pass.getBytes("UTF-8");
        for (int i = 0; i < 8; i++) k[i] = i < p.length ? p[i] : 0;
        return SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(k));
    }

    static String[] encrypt(String txt, SecretKey key) throws Exception {
        Cipher c = Cipher.getInstance("DES/CBC/PKCS5Padding");
        byte[] iv = new byte[8]; new SecureRandom().nextBytes(iv);
        c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        return new String[]{
            Base64.getEncoder().encodeToString(c.doFinal(txt.getBytes("UTF-8"))),
            Base64.getEncoder().encodeToString(iv)
        };
    }

    static String decrypt(String ct, String iv, SecretKey key) throws Exception {
        Cipher c = Cipher.getInstance("DES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Base64.getDecoder().decode(iv)));
        return new String(c.doFinal(Base64.getDecoder().decode(ct)), "UTF-8");
    }

    public static void main(String[] a) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter plaintext: "); String pt = sc.nextLine();
            System.out.print("Enter password (min 8 chars): "); String pw = sc.nextLine();
            SecretKey key = keyFromPassword(pw);
            String[] enc = encrypt(pt, key);
            System.out.println("\nCiphertext: " + enc[0] + "\nIV: " + enc[1]);
            System.out.println("Decrypted: " + decrypt(enc[0], enc[1], key));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
