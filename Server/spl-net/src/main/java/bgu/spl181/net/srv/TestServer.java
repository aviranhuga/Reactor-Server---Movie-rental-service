package bgu.spl181.net.srv;

import bgu.spl181.net.impl.echo.EchoProtocol;
import bgu.spl181.net.impl.echo.LineMessageEncoderDecoder;

public class TestServer {
    public static void main(String[] args){
        Server<String> server = Server.threadPerClient(Integer.parseInt(args[0]),()->new EchoProtocol(),()->new LineMessageEncoderDecoder());
        server.serve();
    }
}
