package pt.ulisboa.tecnico.cmov.airdesk_cmov.NetworkedVersion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities.NetworkActivity;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private Channel channel;
    private NetworkActivity networkActivity;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, NetworkActivity nActivity){

        super();
        this.manager = manager;
        this.channel = channel;
        this.networkActivity = nActivity;

    }

    //notificações sobre o estado da rede p2p
    @Override
    public void onReceive(Context context, Intent intent){

        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(networkActivity, "WiFi Direct enabled",
                Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(networkActivity, "WiFi Direct disable",
                Toast.LENGTH_SHORT).show();

            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            //TODO

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            //TODO
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            //TODO
        }

    }
}
