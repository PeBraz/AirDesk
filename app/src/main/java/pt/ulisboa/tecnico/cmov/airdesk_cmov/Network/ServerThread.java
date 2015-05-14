package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities.FilesActivity;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Application;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.ApplicationOwner;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.StorageOverLimitException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.File;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.CreateFileMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.DeleteFileMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.FilesMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.FilesMessageReply;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.FindWorkspaceMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.FindWorkspaceReplyMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.InviteMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.LockReadFileMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.LockReadFileMessageReply;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.Message;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.MyWorkspacesMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.PingMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.PongMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.ReadFileMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.ReadFileMessageReply;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.WriteFileMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.WorkspaceDto;

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
            System.out.println("My server socket went down" + e.getMessage());
            if (server != null) {
                try {
                    server.close();
                    synchronized (conns){
                        for (Socket s : conns){
                            s.close();
                        }
                    }
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
    /*
    *   Connect to a listening socket
    */
    private static Thread clientThread(final InetAddress ip, final int port) {
        return new Thread(){

            @Override
            public void run() {
                Socket sock;
                try {
                    sock = new Socket(ip, port);
                    clientThread(sock).start();
                } catch (IOException e) {
                    System.out.println("Attempted connection to: " + ip + ", but failed.");
                    return;
                }

                if(isGroupOwner(ip)){
                        send(new PingMessage(Application.getOwner().getEmail()),sock);
                }
            }
        };
    }


    private static Thread clientThread(final Socket sock) {
        return new Thread(){

            @Override
            public void run() {
                synchronized (conns){
                    conns.add(sock);
                    listIP.add(sock.getInetAddress());
                }

                ObjectInputStream ois;
                try {
                    while (true) {
                        ois = new ObjectInputStream(sock.getInputStream());
                        Message msg = (Message) ois.readObject();
                        ServerThread.handleMsg(msg, sock);
                    }
                } catch (IOException e) {
                    System.out.println("Client Thread: " + e.getMessage());
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

    public static boolean send(Message message, Socket socket)  {
        System.out.println("send message started");
        try {
            new ObjectOutputStream(socket.getOutputStream()).writeObject(message);
            return true;
        }catch(IOException e){
            System.out.println("Failed to send a message" + e);
            System.out.println("Socket removed: " + socket.getInetAddress());
            synchronized (conns){
                conns.remove(socket);
            }
            return false;
        }
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
                Application.getPeer(msge.getOwner()).addTags(msge.getWorkspaces());
                break;
            case FILES_MESSAGE:
                FilesMessage msgee = (FilesMessage) msg;
                List<String> lista = Application.getOwner().getWorkspace(msgee.getWorkspaceName()).getFiles();
                send(new FilesMessageReply(Application.getOwner().getEmail(), msgee.getWorkspaceName(),lista), sock);
                break;
            case FILES_MESSAGE_REPLY:
                FilesMessageReply msger = (FilesMessageReply) msg;
                Application.getPeer(msger.getEmail()).setFiles(msger.getWorkspace(), msger.getFiles());
                break;
            case CREATE_FILE_MESSAGE:
                CreateFileMessage cfmsg = (CreateFileMessage) msg;
                Workspace.createFile(cfmsg.getTitle(), cfmsg.getWorkspace(), Application.getOwner().getEmail());
                break;

            case READ_FILE_MESSAGE:
                ReadFileMessage rfmsg = (ReadFileMessage) msg;
                String text = FilesActivity.readFileStorage(Application.getOwner().getEmail(), rfmsg.getWsname(), rfmsg.getFilename());
                send(new ReadFileMessageReply(Application.getOwner().getEmail(), text), sock);
                break;
            case READ_FILE_MESSAGE_REPLY:
                ReadFileMessageReply rfrmsg = (ReadFileMessageReply) msg;
                Application.getPeer(rfrmsg.getEmail()).setFileBody(rfrmsg.getText());
                break;
            case LOCK_READ_FILE_MESSAGE:
                LockReadFileMessage lrfmsg = (LockReadFileMessage) msg;
                System.out.println("USER WANTS LOCK for : " + lrfmsg.getFilename());
                Workspace ws = Application.getOwner().getWorkspace(lrfmsg.getWsname());
                String text2 = FilesActivity.readFileStorage(Application.getOwner().getEmail(), lrfmsg.getWsname(), lrfmsg.getFilename());
                String lockVal = ws.lock(lrfmsg.getFilename());



                //lockVal is null if unable to get lock
                send(new LockReadFileMessageReply(Application.getOwner().getEmail(), lockVal==null? "": text2, lockVal), sock);

                break;
            case LOCK_READ_FILE_MESSAGE_REPLY:
                LockReadFileMessageReply lrfrmsg = (LockReadFileMessageReply) msg;
                //if lock was acquired successfully, change peer state

                if (lrfrmsg.getKey() != null) {
                    System.out.println("Received Lock");
                    Application.getPeer(lrfrmsg.getEmail()).setKey(lrfrmsg.getKey(), lrfrmsg.getText());
                }else System.out.println("did not receive lock");
                break;

            case WRITE_FILE_MESSAGE:
                WriteFileMessage wfmsg = (WriteFileMessage) msg;
                Workspace ws1 = Application.getOwner().getWorkspace(wfmsg.getWorkspace());
                try {
                    System.out.println("foreign user wants to unlock file");
                    if (ws1.unlock(wfmsg.getFilename(), wfmsg.getKey())) {
                        System.out.println("Unlocked, text: " + wfmsg.getText());
                        FilesActivity.fileWrite(Application.getOwner().getEmail(), wfmsg.getWorkspace(), wfmsg.getFilename(), wfmsg.getText());
                    }
                } catch (StorageOverLimitException e) {
                    e.printStackTrace();
                }
                break;
            case DELETE_FILE_MESSAGE:
                DeleteFileMessage dfmsg = (DeleteFileMessage) msg;
                FilesActivity.fileDelete(Application.getOwner().getEmail(), dfmsg.getWorkspace(), dfmsg.getFilename());
                break;
            case INVITE_MESSAGE:
                InviteMessage imsg = (InviteMessage) msg;
                Application.subscribe(new WorkspaceDto(imsg.getEmail(), imsg.getWorkspace()));
                break;
        }
    }

    public static boolean isGroupOwner(InetAddress ip){

        return WiFiDirectBroadcastReceiver.groupOwnerIp != null
                && WiFiDirectBroadcastReceiver.groupOwnerIp == ip;
    }
}
