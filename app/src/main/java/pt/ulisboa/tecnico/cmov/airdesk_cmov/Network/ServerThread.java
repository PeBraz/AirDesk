package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Application;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.ApplicationOwner;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.FindWorkspaceMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.FindWorkspaceReplyMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.Message;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.MyWorkspacesMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.PingMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.PongMessage;

public class ServerThread extends Thread{

    public static final List<Socket> conns = new ArrayList<>();
    private static final List<InetAddress> listIP = new ArrayList<>();

    private int port;
    private static ServerSocket server = null;

    public static final int PORT = 6066;

    public ServerThread(int port){
        this.port = port;
    }

    @Override
    public void run() {

        try {
            ServerThread.server = new ServerSocket();
            server.setReuseAddress(true);
            server.bind(new InetSocketAddress(port));
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
                    listIP.add(sock.getInetAddress());
                }
                if(isGroupOwner(ip)){
                    try {
                        send(new PingMessage(Application.getOwner().getEmail()),sock);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ObjectInputStream ois;
                try {
                    while (true) {
                        ois = new ObjectInputStream(sock.getInputStream());
                        Message msg = (Message) ois.readObject();
                        ServerThread.handleMsg(msg, sock);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    synchronized (conns) {
                        conns.remove(sock);
                        listIP.remove(sock.getInetAddress());
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
                        ois = new ObjectInputStream(sock.getInputStream());
                        Message msg = (Message) ois.readObject();
                        ServerThread.handleMsg(msg, sock);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    synchronized (conns) {
                        conns.remove(sock);
                        listIP.remove(sock.getInetAddress());
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
    public static void join(InetAddress ip, int port) throws IOException {
        System.out.println("joining group");
        clientThread(ip,port).start();
    }

    public static void connectToAll(List<InetAddress> iList) throws IOException{
        System.out.println("aqui");
        for(InetAddress i : iList){
            System.out.println("IP: " + i);
            join(i, PORT);
        }
    }

    public static void send(Message message, Socket socket) throws IOException {
        System.out.println("send message started");
        new ObjectOutputStream(socket.getOutputStream()).writeObject(message);
    }

    public static void sendPong(Socket s) throws IOException {
        send(new PongMessage(listIP,Application.getOwner().getEmail()),s);
    }

    public static void handleMsg(Message msg, Socket sock) throws IOException {
        switch (msg.getMessageType()) {
            case PING:
                System.out.println("ping received");
                PingMessage ping = (PingMessage) msg;
                System.out.println("OWNER: " + ping.getOwner());
                Application.createPeer(ping.getOwner(), sock);
                sendPong(sock);
                break;
            case PONG:
                System.out.println("pong received");
                PongMessage p = (PongMessage) msg;
                Application.createPeer(p.getOwner(), sock);
                connectToAll(p.allPeers);
                break;
            case FIND_WORKSPACE:
                System.out.println("find workspace message received");
                FindWorkspaceMessage wsMessage = (FindWorkspaceMessage) msg;
                System.out.println("QUERY RECEIVED: " + wsMessage.getQuery());
                break;
            case FIND_WORKSPACE_REPLY:
                System.out.println("workspace reply received");
                FindWorkspaceReplyMessage replyMessage = (FindWorkspaceReplyMessage) msg;
                System.out.println("FOUND WORKSPACES: " + replyMessage.getWorkspaces());
                break;
            case MY_WORKSPACES:
                System.out.println("workspaces received");
                MyWorkspacesMessage msge = (MyWorkspacesMessage) msg;
                System.out.println("MEEAS " + msge.getOwner());
                Application.getPeer(msge.getOwner()).addTags(msge.getWorkspaces());
        }
    }

    public static boolean isGroupOwner(InetAddress ip){

        return WiFiDirectBroadcastReceiver.groupOwnerIp != null
                && WiFiDirectBroadcastReceiver.groupOwnerIp == ip;
    }
}
