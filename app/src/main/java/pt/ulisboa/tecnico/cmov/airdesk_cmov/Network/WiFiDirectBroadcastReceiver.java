package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities.MainActivity;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private Channel channel;
    private MainActivity activity;
    public static List<WifiP2pDevice> peers = new ArrayList<>();

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, MainActivity mActivity){

        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = mActivity;

    }

    @Override
    public void onReceive(Context context, Intent intent){

        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                activity.setWifiConnection(true);
                Toast.makeText(activity, "WiFi enabled", Toast.LENGTH_SHORT).show();
            }
            else {
                activity.setWifiConnection(false);
                Toast.makeText(activity, "WiFi disable", Toast.LENGTH_SHORT).show();
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            if (manager != null) {
                manager.requestPeers(channel, getListListener());
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            if (manager == null) {
                return;
            }

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                manager.requestConnectionInfo(channel, getInfoListener());
            }

            else System.out.println("AQUIII");

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            //TODO
        }
    }

    public WifiP2pManager.ConnectionInfoListener getInfoListener() {
        return new WifiP2pManager.ConnectionInfoListener() {
            @Override
            public void onConnectionInfoAvailable(final WifiP2pInfo info) {

                System.out.println("ownerAddress: " + info.groupOwnerAddress);
                System.out.println("Is group formed: " + info.groupFormed);

                InetAddress groupOwnerAddress = info.groupOwnerAddress;
                new ServerThread(15002).start();

                if (info.groupFormed && info.isGroupOwner) {
                    System.out.println("SOU O GROUP OWNER");

                } else if (info.groupFormed) {
                    ServerThread.join(groupOwnerAddress, 15002); //group owner connection
                }
            }
        };
    }

    public WifiP2pManager.PeerListListener getListListener() {
        return new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerList) {

                peers.clear();
                peers.addAll(peerList.getDeviceList());

                for(WifiP2pDevice device : peerList.getDeviceList()){
                    System.out.println("DEVICE NAME: " + device.deviceName);
                }

                if(peers.size() == 0) System.out.println("No devices found.");
            }
        };
    }

}
