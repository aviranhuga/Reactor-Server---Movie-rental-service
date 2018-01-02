package bgu.spl181.net.impl.TestServer;

import bgu.spl181.net.impl.echo.EchoProtocol;
import bgu.spl181.net.srv.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TestServer {

    public static void main(String[] args) throws IOException {

//        Server.threadPerClient(
//                7777,
//                ()->new TestProtocol(),
//                ()->new TestMessageEncoderDecoder()
//        ).serve();
        Server.reactor(6,
                7777,
                ()->new TestProtocol(),
                ()->new TestMessageEncoderDecoder()
        ).serve();

    }
}
