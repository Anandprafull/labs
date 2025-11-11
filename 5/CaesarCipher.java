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
