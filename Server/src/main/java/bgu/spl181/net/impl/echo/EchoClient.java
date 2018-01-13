

package bgu.spl181.net.impl.echo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class EchoClient {


    //configurable params
    private static int randomMultConstant = 15;
    private static int numOfCopies = 7;
    private static int numOfClients = 18;
    private static int numOfIterations = 100;
    private static Boolean testNumOfRentedCopies = false;

    //test vars
    private static int numOfRentedCopies=0;
    private static ArrayList<Thread> clientThreads = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        //clientThreads creation
        for(int i = 0 ; i < numOfIterations; i++){

            clientThreads.clear();
            for(int j=0;j<numOfClients;j++){
                final int num = j;
                Thread client= new Thread(() -> {
                    try {
                        normalClient("Client"+num);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                clientThreads.add(client);
            }
            adminClient("admin");

            //starting clients
            for(Thread client : clientThreads){
                client.start();
            }
            EchoClient.waitForThreads();
            EchoClient.returnAllCopies();

        }
        System.out.println("Test ended.");
    }
    //holds the client config
    private static void normalClient(String client) throws IOException {
        try (Socket sock = new Socket("localhost", 7777);
             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {
            sleep((int)(Math.random()* randomMultConstant));
            ArrayList<String> cmds = new ArrayList<>();
            cmds.add("REGISTER "+client+" tryingagain country=\"Russia\"\n");
            cmds.add("LOGIN "+client+" tryingagain\n");
            cmds.add("REQUEST balance add 10\n");
            cmds.add("REQUEST info \"Alice in wonderland\"\n");
            cmds.add("REQUEST rent \"The Godfather\"\n");
            cmds.add("SIGNOUT\n");


            for (String cmd : cmds) {
                sleep((int) (Math.random()* EchoClient.randomMultConstant));
                out.write(cmd);
                out.flush();
                String line = in.readLine();

                if (line.equals("ACK signout succeeded")) {
                    sock.close();
                    System.out.println(client+": disconnected");
                    return;
                }

                System.out.println(client+": " + line);
                while (true) {
                    if (line.contains("BROADCAST")) {
                        line = in.readLine();
                        System.out.println(client+": " + line);
                    } else
                        break;
                }
                if(line.equals("ACK rent \"The Godfather\" success"))
                    EchoClient.numOfRentedCopies++;
                if(cmd.equals("SIGNOUT") && !line.equals("ACK signout succeeded")){
                    System.out.println("fff");
                }
                if (line.equals("ACK signout succeeded")) {
                    sock.close();
                    System.out.println(client+": disconnected");
                    break;
                }


            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void waitForThreads() {
        for(Thread client: clientThreads){
            try {
                client.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void returnAllCopies() throws Exception {
        if (testNumOfRentedCopies && numOfCopies!=numOfRentedCopies) {
            System.out.println("numOfCopies: "+numOfCopies);
            System.out.println("numOfRentedCopies: "+numOfRentedCopies);
            throw new Exception("numOfCopies!=numOfRentedCopies");
        }
        for(int j=0;j<numOfClients;j++){
            EchoClient.clientReturn("Client"+j);
        }
        //assumes that all return callouts went well
        numOfRentedCopies=0;
    }

    private static void adminClient(String client) throws IOException {

        try (Socket sock = new Socket("localhost", 7777);
             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {

            ArrayList<String> cmds = new ArrayList<>();
            cmds.add("LOGIN admin admin\n");
            cmds.add("REQUEST balance add 1\n");
            cmds.add("REQUEST addmovie \"Matrix\" "+numOfCopies+" 40 \"Italy\"\n");
            cmds.add("REQUEST addmovie \"The Godfather\" "+numOfCopies+" 2\n");
            cmds.add("REQUEST remmovie \"Matrix\" \n");
            cmds.add("REQUEST addmovie \"Alice in wonderland\" "+numOfCopies+" 12\n");
            cmds.add("REQUEST info \"Alice in wonderland\"\n");
            cmds.add("SIGNOUT\n");;

            for (String cmd : cmds) {
                out.write(cmd);
                out.flush();
                String line = in.readLine();
                System.out.println(client+": " + line);
                while (true) {
                    if (line.contains("BROADCAST")) {
                        line = in.readLine();
                        System.out.println(client+": " + line);
                    } else
                        break;
                }

                if (line.equals("ACK signout succeeded")) {
                    sock.close();
                    System.out.println(client+": disconnected----------------------------------");
                }


            }
        }
    }

    private static void clientReturn(String client) throws IOException {
        try (Socket sock = new Socket("localhost", 7777);
             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {
            ArrayList<String> cmds = new ArrayList<>();
            cmds.add("LOGIN "+client+" tryingagain\n");
            cmds.add("REQUEST return \"The Godfather\" \n");
            cmds.add("SIGNOUT\n");


            for (String cmd : cmds) {
                sleep((long) (Math.random()* EchoClient.randomMultConstant));
                out.write(cmd);
                out.flush();
                String line = in.readLine();
                System.out.println(client+": " + line);
                while (true) {
                    if(line==null)
                        System.out.println("WEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
                    if (line.contains("BROADCAST")) {
                        line = in.readLine();
                        System.out.println(client+": " + line);
                    } else
                        break;
                }
                if (line.equals("ACK signout succeeded")) {
                    sock.close();
                    System.out.println(client+": disconnected");
                    break;
                }


            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}



//package bgu.spl181.net.impl.echo;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.Socket;
//import java.util.ArrayList;
//
//import static java.lang.Thread.sleep;
//
//public class EchoClient {
//
//
//    //configurable params
//    private static int randomMultConstant = 15;
//    private static int numOfCopies = 7;
//    private static int numOfClients = 18;
//    private static int numOfIterations = 1;
//    private static Boolean testNumOfRentedCopies = false;
//
//    //test vars
//    private static int numOfRentedCopies=0;
//    private static ArrayList<Thread> clientThreads = new ArrayList<>();
//
//    public static void main(String[] args) throws Exception {
//
//        //clientThreads creation
//        for(int i = 0 ; i < numOfIterations; i++){
//
//            clientThreads.clear();
//                Thread client= new Thread(() -> {
//                    try {
//                        normalClient("Client0");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//            clientThreads.clear();
//            Thread client1= new Thread(() -> {
//                try {
//                    normalClient1("Client1");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//            clientThreads.clear();
//            Thread client2= new Thread(() -> {
//                try {
//                    normalClient2("Client2");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//            clientThreads.clear();
//            Thread client3= new Thread(() -> {
//                try {
//                    normalClient3("Client3");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//                clientThreads.add(client);
//            clientThreads.add(client1);
//            clientThreads.add(client2);
//            clientThreads.add(client3);
//            adminClient("admin");
//
//            //starting clients
//            client.start();
//                client1.start();
//            client2.start();
//            client3.start();
//            EchoClient.waitForThreads();
//            adminClient1("admin");
//
//        }
//        System.out.println("Test ended.");
//    }
//    //holds the client config
//    private static void normalClient(String client) throws IOException {
//        try (Socket sock = new Socket("localhost", 7777);
//             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {
//            sleep((int)(Math.random()* randomMultConstant));
//            ArrayList<String> cmds = new ArrayList<>();
//            cmds.add("REGISTER "+client+" tryingagain country=\"Russia\"\n");
//            cmds.add("LOGIN "+client+" tryingagain\n");
//            cmds.add("REQUEST balance add 15\n");
//            cmds.add("REQUEST rent \"Alice in wonderland\"\n");
//            cmds.add("REQUEST rent \"The Godfather\"\n");
//            cmds.add("REQUEST return \"Alice in wonderland\"\n");
//            cmds.add("REQUEST return \"The Godfather\"\n");
//            cmds.add("SIGNOUT\n");
//
//
//            for (String cmd : cmds) {
//                sleep((int) (Math.random()* EchoClient.randomMultConstant));
//                out.write(cmd);
//                out.flush();
//                String line = in.readLine();
//                System.out.println(client+": " + line);
//                while (true) {
//                    if (line.contains("BROADCAST")) {
//                        line = in.readLine();
//                        System.out.println(client+": " + line);
//                    } else
//                        break;
//                }
//                if (line.equals("ACK signout succeeded")) {
//                    sock.close();
//                    System.out.println(client+": disconnected");
//                    break;
//                }
//
//
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void normalClient1(String client) throws IOException {
//        try (Socket sock = new Socket("localhost", 7777);
//             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {
//            sleep((int)(Math.random()* randomMultConstant));
//            ArrayList<String> cmds = new ArrayList<>();
//            cmds.add("REGISTER "+client+" tryingagain country=\"Russia\"\n");
//            cmds.add("LOGIN "+client+" tryingagain\n");
//            cmds.add("REQUEST balance add 50\n");
//            cmds.add("REQUEST info \"Gay dor\"\n");
//            cmds.add("REQUEST rent \"Matrix\"\n");
//            cmds.add("REQUEST return \"Matrix\"\n");
//            cmds.add("SIGNOUT\n");
//
//
//            for (String cmd : cmds) {
//                sleep((int) (Math.random()* EchoClient.randomMultConstant));
//                out.write(cmd);
//                out.flush();
//                String line = in.readLine();
//                System.out.println(client+": " + line);
//                while (true) {
//                    if (line.contains("BROADCAST")) {
//                        line = in.readLine();
//                        System.out.println(client+": " + line);
//                    } else
//                        break;
//                }
//                if (line.equals("ACK signout succeeded")) {
//                    sock.close();
//                    System.out.println(client+": disconnected");
//                    break;
//                }
//
//
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void normalClient2(String client) throws IOException {
//        try (Socket sock = new Socket("localhost", 7777);
//             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {
//            sleep((int)(Math.random()* randomMultConstant));
//            ArrayList<String> cmds = new ArrayList<>();
//            cmds.add("REGISTER "+client+" tryingagain country=\"Russia\"\n");
//            cmds.add("LOGIN "+client+" tryingagain\n");
//            cmds.add("REQUEST balance add 10\n");
//            cmds.add("REQUEST rent \"Gay dor\"\n");
//            cmds.add("REQUEST rent \"The Godfather\"\n");
//            cmds.add("REQUEST rent \"Matrix\"\n");
//            cmds.add("REQUEST return \"Gay dor\"\n");
//            cmds.add("REQUEST return \"The Godfather\"\n");
//            cmds.add("REQUEST return \"Matrix\"\n");
//            cmds.add("SIGNOUT\n");
//
//
//            for (String cmd : cmds) {
//                sleep((int) (Math.random()* EchoClient.randomMultConstant));
//                out.write(cmd);
//                out.flush();
//                String line = in.readLine();
//                System.out.println(client+": " + line);
//                while (true) {
//                    if (line.contains("BROADCAST")) {
//                        line = in.readLine();
//                        System.out.println(client+": " + line);
//                    } else
//                        break;
//                }
//                if (line.equals("ACK signout succeeded")) {
//                    sock.close();
//                    System.out.println(client+": disconnected");
//                    break;
//                }
//
//
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void normalClient3(String client) throws IOException {
//        try (Socket sock = new Socket("localhost", 7777);
//             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {
//            sleep((int)(Math.random()* randomMultConstant));
//            ArrayList<String> cmds = new ArrayList<>();
//            cmds.add("REGISTER "+client+" tryingagain country=\"Russia\"\n");
//            cmds.add("LOGIN "+client+" tryingagain\n");
//            cmds.add("REQUEST balance add 10\n");
//            cmds.add("REQUEST rent \"Gay dor\"\n");
//            cmds.add("REQUEST rent \"The Godfather\"\n");
//            cmds.add("REQUEST rent \"Matrix\"\n");
//            cmds.add("REQUEST rent \"Alice in wonderland\"\n");
//            cmds.add("REQUEST return \"Gay dor\"\n");
//            cmds.add("REQUEST return \"The Godfather\"\n");
//            cmds.add("REQUEST return \"Matrix\"\n");
//            cmds.add("REQUEST return \"Alice in wonderland\"\n");
//            cmds.add("SIGNOUT\n");
//
//
//            for (String cmd : cmds) {
//                sleep((int) (Math.random()* EchoClient.randomMultConstant));
//                out.write(cmd);
//                out.flush();
//                String line = in.readLine();
//                System.out.println(client+": " + line);
//                while (true) {
//                    if (line.contains("BROADCAST")) {
//                        line = in.readLine();
//                        System.out.println(client+": " + line);
//                    } else
//                        break;
//                }
//                if (line.equals("ACK signout succeeded")) {
//                    sock.close();
//                    System.out.println(client+": disconnected");
//                    break;
//                }
//
//
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void waitForThreads() {
//        for(Thread client: clientThreads){
//            try {
//                client.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private static void adminClient(String client) throws IOException {
//
//        try (Socket sock = new Socket("localhost", 7777);
//             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {
//
//            ArrayList<String> cmds = new ArrayList<>();
//            cmds.add("LOGIN admin admin\n");
//            cmds.add("REQUEST balance add 1\n");
//            cmds.add("REQUEST addmovie \"Matrix\" 5 1 \"Italy\"\n");
//            cmds.add("REQUEST addmovie \"The Godfather\" 3 2\n");
//            cmds.add("REQUEST addmovie \"Gay dor\" 2 2\n");
//            cmds.add("REQUEST addmovie \"Alice in wonderland\" 2 12\n");
//            cmds.add("REQUEST info \"Alice in wonderland\"\n");
//            cmds.add("SIGNOUT\n");
//
//            for (String cmd : cmds) {
//                out.write(cmd);
//                out.flush();
//                String line = in.readLine();
//                System.out.println(client+": " + line);
//                while (true) {
//                    if (line.contains("BROADCAST")) {
//                        line = in.readLine();
//                        System.out.println(client+": " + line);
//                    } else
//                        break;
//                }
//
//                if (line.equals("ACK signout succeeded")) {
//                    sock.close();
//                    System.out.println(client+": disconnected----------------------------------");
//                }
//
//
//            }
//        }
//    }
//
//    private static void adminClient1(String client) throws IOException {
//
//        try (Socket sock = new Socket("localhost", 7777);
//             BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))) {
//
//            ArrayList<String> cmds = new ArrayList<>();
//            cmds.add("LOGIN admin admin\n");
//            cmds.add("REQUEST balance add 1\n");
//            cmds.add("REQUEST remmovie \"Matrix\"\n");
//            cmds.add("REQUEST remmovie \"The Godfather\"\n");
//            cmds.add("REQUEST remmovie \"Gay dor\"\n");
//            cmds.add("REQUEST remmovie \"Alice in wonderland\"\n");
//            cmds.add("REQUEST info\n");
//            cmds.add("SIGNOUT\n");
//
//            for (String cmd : cmds) {
//                out.write(cmd);
//                out.flush();
//                String line = in.readLine();
//                if (cmd.equals("info")) {
//                    System.out.println("__________________________________________________________________");
//                    System.out.println(client + ": " + line);
//                    while (true) {
//                        if (line.contains("BROADCAST")) {
//                            line = in.readLine();
//                            System.out.println(client + ": " + line);
//                        } else
//                            break;
//                    }
//
//                    if (line.equals("ACK signout succeeded")) {
//                        sock.close();
//                        System.out.println(client + ": disconnected----------------------------------");
//                    }
//
//
//                }
//            }
//        }
//    }
//}