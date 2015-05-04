package pt.ulisboa.tecnico.cmov.airdesk_cmov.NetworkedVersion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities.MainActivity;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private Channel channel;
    private MainActivity activity;
    private List<WifiP2pDevice> peers = new ArrayList();

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, MainActivity mActivity){

        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = mActivity;

    }

    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            peers.clear();
            peers.addAll(peerList.getDeviceList());
        }
    };

    @Override
    public void onReceive(Context context, Intent intent){

        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(activity, "WiFi Direct enabled",
                Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(activity, "WiFi Direct disable",
                Toast.LENGTH_SHORT).show();
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            if (manager != null) {
                manager.requestPeers(channel, peerListListener);
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
            //TODO
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
            //TODO
        }
    }

    public void connect() {
        WifiP2pDevice device = peers.get(0);

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        manager.connect(channel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {

            }
        });
    }
}
