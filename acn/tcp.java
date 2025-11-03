//tcps.java
import java.net.*;
import java.io.*;

public class tcps{
	public static void main(String[] args ) throws Exception{
		ServerSocket sersock=new ServerSocket(5001);
		System.out.println("Server ready for connection");
		
		Socket sock = sersock.accept();
		System.out.println("Connection successful and waiting for chatting");
		InputStream istream = sock.getInputStream();
		BufferedReader fileRead=new BufferedReader(new
InputStreamReader(istream));
String fname=fileRead.readLine();
BufferedReader ContentRead=new BufferedReader(new
FileReader(fname));
OutputStream ostream=sock.getOutputStream();
PrintWriter pwrite=new PrintWriter(ostream,true);
String str;
while((str=ContentRead.readLine())!=null){
pwrite.println(str);
}
sock.close();
sersock.close();
pwrite.close();
fileRead.close();
ContentRead.close();
}
}
// tcpc.java
import java.net.*;
import java.io.*;
public class TCPC
{
public static void main(String[] args) throws Exception
{
Socket sock=new Socket("127.0.0.1",5001);
System.out.println("Enter the filename");
BufferedReader keyRead=new BufferedReader(new InputStreamReader
(System.in));
String fname=keyRead.readLine();
OutputStream ostream=sock.getOutputStream();
PrintWriter pwrite=new PrintWriter(ostream,true);
pwrite.println(fname);
InputStream istream=sock.getInputStream();
BufferedReader socketRead=new BufferedReader(new
InputStreamReader(istream));
String str;
while((str=socketRead.readLine())!=null)
{
System.out.println(str);
}
pwrite.close();
socketRead.close();
keyRead.close();
}
}
/*
  java TCPS
  java TCPC
  Enter the filename sample.java
  Helloo Every one!
*/
