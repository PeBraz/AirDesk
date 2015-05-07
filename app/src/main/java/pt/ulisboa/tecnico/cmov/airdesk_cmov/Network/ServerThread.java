package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.Message;

public class ServerThread extends Thread{

    private static final List<Socket> conns = new ArrayList<>();

    private int port;
    private static ServerSocket server = null;

    public ServerThread(int port){
        this.port = port;
    }

    @Override
    public void run() {

        try {
            ServerThread.server = new ServerSocket(port);
            System.out.println("Listening on port: " + port);

            while (true) {
                Socket sock = server.accept();
                synchronized (ServerThread.conns){
                    conns.add(sock);
                }
                clientThread(sock).start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void getOut() throws IOException {
        if (server != null) {
            ServerThread.server.close();
        }
    }

    private static Thread clientThread(final InetAddress ip, final int port) {
        return new Thread(){

            @Override
            public void run() {
                Socket sock;
                try {
                    sock = new Socket(ip, port);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                synchronized (conns) {
                    conns.add(sock);
                }
                ObjectInputStream ois;
                try {
                    while (true) {
                        ois = new ObjectInputStream(sock.getInputStream()); //receber mensagens
                        Message msg = (Message) ois.readObject();
                        ServerThread.handleMsg(msg, sock);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    synchronized (conns) {
                        conns.remove(sock);
                    }
                    try {
                        sock.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private static Thread clientThread(final Socket sock) {
        return new Thread(){

            @Override
            public void run() {

                ObjectInputStream ois;
                try {
                    while (true) {
                        ois = new ObjectInputStream(sock.getInputStream()); //receber mensagens
                        Message msg = (Message) ois.readObject();
                        ServerThread.handleMsg(msg, sock);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    synchronized (conns) {
                        conns.remove(sock);
                    }
                    try {
                        sock.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
    }
    public static void join(InetAddress ip, int port) {

        clientThread(ip,port).start();
    }

    public static void send(Message message, Socket socket) throws IOException {
        System.out.println("entrou no send");
        new ObjectOutputStream(socket.getOutputStream()).writeObject(message);

    }

    public static void handleMsg(Message msg, Socket sock) throws IOException{
        System.out.println(msg.toString() + sock.toString());
    }
}
