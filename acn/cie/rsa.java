import java.util.*;
import java.math.*;
import java.io.*;

public class rsa{
        public static void main(String[] args) {
                Random r = new Random();
                BigInteger p = BigInteger.probablePrime(512,r);
                BigInteger q = BigInteger.probablePrime(512,r);

                BigInteger n = p.multiply(q);

                BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

                BigInteger e = BigInteger.probablePrime(256,r);

                while(phi.gcd(e).intValue()>1)  e=e.add(BigInteger.ONE);

                BigInteger d=e.modInverse(phi);

                System.out.print("Text: ");
                String s = new Scanner(System.in).nextLine();

                BigInteger enc = new BigInteger(s.getBytes()).modPow(e,n);
                BigInteger dec = enc.modPow(d,n);

                System.out.println("Encrypted: " +enc);
                System.out.println("Decrypted: " +new String(dec.toByteArray()));
        }
} 


