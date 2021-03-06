package man.by;

import man.by.frames.FrameReader;
import man.by.frames.FrameWriter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    private boolean shutdown = false;
    private int port;
    private ServerSocket serverSocket;


    public static void main(String[] args) {
        Server server = new Server();
        server.setPort(80);
        server.await();
    }

    public void setPort(int port) {
        this.port = port;
    }

    private void await() {
        System.out.println("Server listening on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(this.port)){
            boolean wersockrt = false;
            Socket socket = serverSocket.accept();
            while (true){

                try {

                    while (wersockrt){
                        FrameReader fr = new FrameReader(socket);

                        FrameWriter fw = new FrameWriter(fr);

                        wersockrt = fw.isClose();

                        if(!wersockrt){
                            socket = serverSocket.accept();
                        }
                    }



                    HttpRequest req = new HttpRequest(socket);

                    HttpResponse res = new HttpResponse();

                    wersockrt = res.HttpResponseChecker(req);



                } catch (IOException e) {
                    e.printStackTrace();
                }

          }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

