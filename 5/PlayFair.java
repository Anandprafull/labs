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
