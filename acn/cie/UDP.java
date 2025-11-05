import java.net.*;

class UDPServer {
    public static void main(String[] args) throws Exception {
        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] buffer = new byte[1024];
        System.out.println("Server is running...");

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(receivePacket);

            String receivedMsg = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("FROM CLIENT: " + receivedMsg);

            InetAddress ip = receivePacket.getAddress();
            int port = receivePacket.getPort();

            String responseMsg = "SERVER MSG: Acknowledged! | RESULT: " + receivedMsg.toUpperCase();
            byte[] sendData = responseMsg.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
            serverSocket.send(sendPacket);
        }
    }
}

import java.io.*;
import java.net.*;

class UDPClient {
    public static void main(String[] args) throws Exception {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress ip = InetAddress.getByName("localhost");

        System.out.print("Enter a string: ");
        String sentence = inFromUser.readLine();

        byte[] sendData = sentence.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, 9876);
        clientSocket.send(sendPacket);

        byte[] buffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
        clientSocket.receive(receivePacket);

        String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());
        System.out.println("FROM SERVER: " + serverResponse);

        clientSocket.close();
    }
}
