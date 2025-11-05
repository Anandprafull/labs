import java.net.*;
import java.io.*;

class TCPS {
    public static void main(String[] a) throws Exception {
        var s = new ServerSocket(4000).accept();
        try {
            new FileInputStream(
              new BufferedReader(new InputStreamReader(s.getInputStream())).readLine()).transferTo(s.getOutputStream());
        } catch (Exception e) {
            s.getOutputStream().write("Error: File not found.".getBytes());
        }
    }
}

import java.net.*;
import java.io.*;

class TCPC {
    public static void main(String[] a) throws Exception {
        var s = new Socket("localhost", 4000);
        new PrintWriter(s.getOutputStream(), true).println(new BufferedReader(new InputStreamReader(System.in)).readLine());
        s.getInputStream().transferTo(System.out);
    }
}
