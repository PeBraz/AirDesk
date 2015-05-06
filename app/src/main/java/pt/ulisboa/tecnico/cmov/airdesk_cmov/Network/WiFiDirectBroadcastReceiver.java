package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities.MainActivity;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.File;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.Message;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.PingMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.User;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private Channel channel;
    private MainActivity activity;
    private List<WifiP2pDevice> peers = new ArrayList();
    private final List<Socket> conns = new ArrayList<>();
    private final int DEFAULT_PORT = 9876;
    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, MainActivity mActivity){

        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = mActivity;

    }

    private WifiP2pManager.PeerListListener getPeerListListener() {
        return new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerList) {
                peers.clear();
                peers.addAll(peerList.getDeviceList());
            }
        };
    }

    @Override
    public void onReceive(Context context, Intent intent){

        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {

                Toast.makeText(activity, "WiFi Direct enabled",
                Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(activity, "WiFi Direct disable",
                Toast.LENGTH_SHORT).show();
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            if (manager != null) {
                manager.requestPeers(channel, getPeerListListener());
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            manager.requestConnectionInfo(channel, getConnectionInfoListener());

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            //TODO
        }
    }
    /**
     * Detect peers that are in range in the network
     */
    void discoverPeers(){
        this.manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int reasonCode) {
            }
        });
    }

    /**
     * Connects to a group
     *
     */
    public void connect()
    {
        WifiP2pDevice dev = this.peers.get(0);
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = dev.deviceAddress;

        manager.connect(channel, config, new WifiP2pManager.ActionListener(){
            @Override
            public void onSuccess() {

            }
            @Override
            public void onFailure(int errorCode) {

            }
        });
    }
    private WifiP2pManager.ConnectionInfoListener getConnectionInfoListener(){
        return new WifiP2pManager.ConnectionInfoListener(){

            @Override
            public void onConnectionInfoAvailable(final WifiP2pInfo info) {

                InetAddress groupOwnerAddress = info.groupOwnerAddress;
                serverThread(DEFAULT_PORT).start();

                if (info.groupFormed && info.isGroupOwner) {
                    //TODO

                } else if (info.groupFormed) {
                    //TODO
                }
            }
        };

    }

    private Thread serverThread(final int port) {
       return new Thread(){

            @Override
            public  void run() {
                ServerSocket server;
                try {
                    server = new ServerSocket(port);
                    server.bind(null);
                    System.out.println("Listening on port: " + port);

                    while (true) {
                        Socket sock = server.accept();
                        synchronized (this){
                            conns.add(sock);
                        }
                        clientThread(sock).start();
                    }

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.exit(-1);
                }
            }
        };

    }

    public void join(String ip, int port) {
        try {
            Socket sock = new Socket(ip, port);
            synchronized (this) {
                conns.add(sock);
            }
            clientThread(sock).start();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    private Thread clientThread(final Socket sock) {
        return new Thread(){

            @Override
            public void run() {
                ObjectInputStream ois;
                try {
                    while (true) {
                        ois = new ObjectInputStream(sock.getInputStream()); //recebe mensagens
                        Message msg = (Message) ois.readObject();
                        handleMsg(msg, sock);
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
                    System.exit(1);
                }
            }
        };
    }

    public List<User> netUsers = new ArrayList<>();
    public List<File> netFiles = new ArrayList<>();

    public void handleMsg(Message msg, Socket sock) throws IOException{
        switch (msg.getMessageType()){
            case PING:
                PingMessage pmsg = (PingMessage) msg;
                System.out.println("Received message with id: " + pmsg.id);
                User u = new User(pmsg.email);
                netUsers.add(u);
                break;
            case FIND_WORKSPACE:
                break;
            case INVITE:
                break;
        }
/*        switch(msg.getMessageType()) {
            case PING:
                netUsers.add(new User(msg.email));
                break;

            case FILE_INFO:
                netFiles.add();
                break;
            case WORKSPACE_INFO:

                break;

        }*/
    }

}
