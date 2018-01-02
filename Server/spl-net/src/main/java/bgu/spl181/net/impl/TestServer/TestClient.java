package bgu.spl181.net.impl.TestServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class TestClient {

    public static void main(String[] args) throws IOException {


        //BufferedReader and BufferedWriter automatically using UTF-8 encoding
        //int port = Integer.parseInt(args[0]);
        try (Socket sock = new Socket("localhost", 7777);
             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {
            Scanner scanner = new Scanner(System.in);
            Thread Helper = new Thread(()->{
                while(true){
                    String line = "";
                    try {
                        line = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("message from server: " + line);
            }
            });
            Helper.start();
            while(true) {
                out.write(scanner.nextLine());
                System.out.println("sending message to server");
                out.newLine();
                out.flush();

            }
        }
    }
}
