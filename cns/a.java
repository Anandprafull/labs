import java.util.*;

public class CaesarCipher {
    static String f(String t, int s){
      StringBuilder r=new StringBuilder();
      for(char c:t.toCharArray()) 
          if(Character.isLetter(c)){
            char b=Character.isUpperCase(c)?'A':'a';
            r.append((char)((c-b+s)%26+b));
          }
          else r.append(c);   
      return r.toString();
}
    public static void main(String[] a){
        Scanner sc=new Scanner(System.in);
        System.out.print("Text: ");
        String t=sc.nextLine();
        System.out.print("Shift: ");
        int s=sc.nextInt();
        String e=f(t,s),d=f(e,26-s);
        System.out.println("Enc: "+e+"\nDec: "+d);
    }
}
---------------------------------------------------
import java.util.*;

public class PlayfairCipher {
    static char[][] m = new char[5][5];

    static void g(String k) {
        k = k.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        LinkedHashSet<Character> s = new LinkedHashSet<>();
        for (char c : k.toCharArray()) s.add(c);
        for (char c = 'A'; c <= 'Z'; c++) if (c != 'J') s.add(c);
        Iterator<Character> it = s.iterator();
        for (int i = 0; i < 5; i++) for (int j = 0; j < 5; j++) m[i][j] = it.next();
    }

    static int[] p(char c) {
        if (c == 'J') c = 'I';
        for (int i = 0; i < 5; i++) 
            for (int j = 0; j < 5; j++) 
                if (m[i][j] == c) return new int[]{i, j};
        return null;
    }

    static String e(String t) {
        t = t.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < t.length();) {
            char a = t.charAt(i), b = (i + 1 < t.length()) ? t.charAt(i + 1) : 'X';
            if (a == b) b = 'X'; else i++;
            int[] x = p(a), y = p(b);
            if (x[0] == y[0]) r.append(m[x[0]][(x[1] + 1) % 5]).append(m[y[0]][(y[1] + 1) % 5]);
            else if (x[1] == y[1]) r.append(m[(x[0] + 1) % 5][x[1]]).append(m[(y[0] + 1) % 5][y[1]]);
            else r.append(m[x[0]][y[1]]).append(m[y[0]][x[1]]);
            i++;
        }
        return r.toString();
    }

    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        System.out.print("Key: "); g(s.nextLine());
        System.out.print("Msg: "); System.out.println("Enc: " + e(s.nextLine()));
    }
}
---------------------------------

import javax.crypto.*; import javax.crypto.spec.*; 
import java.security.*; import java.util.*;

public class DESDemo {
    static SecretKey key(String p) throws Exception {
        byte[] b = Arrays.copyOf(p.getBytes(), 8);
        return SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(b));
    }
    public static void main(String[] a) throws Exception {
        Scanner s = new Scanner(System.in);
        Cipher c = Cipher.getInstance("DES/CBC/PKCS5Padding");
        byte[] iv = new byte[8]; new SecureRandom().nextBytes(iv);
        SecretKey k = key(s.nextLine());
        c.init(Cipher.ENCRYPT_MODE, k, new IvParameterSpec(iv));
        String e = Base64.getEncoder().encodeToString(c.doFinal(s.nextLine().getBytes()));
        c.init(Cipher.DECRYPT_MODE, k, new IvParameterSpec(iv));
        System.out.println("Enc:" + e + "\nDec:" + new String(c.doFinal(Base64.getDecoder().decode(e))));
    }
}

---------------------------------
import javax.crypto.*; import javax.crypto.spec.*; import java.util.*;
public class AESMini {
    public static void main(String[] a) throws Exception {
        Scanner s = new Scanner(System.in);

        // Key input
        System.out.print("Key(16): "); 
        SecretKeySpec k = new SecretKeySpec(s.nextLine().getBytes(), "AES");

        // Cipher setup
        Cipher c = Cipher.getInstance("AES");

        // Message input
        System.out.print("Msg: "); 
        String m = s.nextLine();

        // Encrypt
        c.init(Cipher.ENCRYPT_MODE, k);
        String e = Base64.getEncoder().encodeToString(c.doFinal(m.getBytes()));
        System.out.println("Enc: " + e);

        // Decrypt
        c.init(Cipher.DECRYPT_MODE, k);
        System.out.println("Dec: " + new String(c.doFinal(Base64.getDecoder().decode(e))));
    }
}
----------------------------
public class RSAMini {
    public static void main(String[] a)throws Exception{
        KeyPairGenerator g=KeyPairGenerator.getInstance("RSA");
        g.initialize(2048); KeyPair k=g.generateKeyPair();
        Cipher c=Cipher.getInstance("RSA");
        String m="Hello, RSA!";
        
        // Encrypt
        c.init(Cipher.ENCRYPT_MODE,k.getPublic());
        String e=Base64.getEncoder().encodeToString(c.doFinal(m.getBytes()));
        System.out.println("Enc:"+e);
        
        // Decrypt
        c.init(Cipher.DECRYPT_MODE,k.getPrivate());
        System.out.println("Dec:"+new String(c.doFinal(Base64.getDecoder().decode(e))));
    }
}
