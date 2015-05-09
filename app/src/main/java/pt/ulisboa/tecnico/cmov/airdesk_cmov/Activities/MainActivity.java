package pt.ulisboa.tecnico.cmov.airdesk_cmov.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Application;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NotRegisteredException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WrongPasswordException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.ServerThread;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.WiFiDirectBroadcastReceiver;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.Message;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.MessageType;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.R;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Sessions.SessionManager;

public class MainActivity extends ActionBarActivity{

    private IntentFilter mIntentFilter;
    private WifiP2pManager mManager;
    private Channel mChannel;
    private BroadcastReceiver mReceiver;
    private boolean isWifiOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        Application.init(getApplicationContext());

        if (Application.session.isLoggedIn()) {
            String email = Application.session.getUserInfo().get(SessionManager.KEY_EMAIL);
            try {
                Application.login(email);
                startActivity(new Intent(this, MyWorkSpacesActivity.class));
            }catch (NotRegisteredException  | WrongPasswordException  e ) {
               System.out.println(e.getMessage());
            }
        }

        Button button = (Button) findViewById(R.id.button6);
        Button button2 = (Button) findViewById(R.id.button5);

        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
        });

        button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
            }
            });

        if (!isWifiOn) Toast.makeText(MainActivity.this, "Wifi is off. Please, enable it.", Toast.LENGTH_SHORT).show();

        this.discoverPeers();
    }

    @Override
    protected void onResume() {

        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        try {
            ServerThread.getOut();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public void discoverPeers(){

        this.mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "Discovery started.", Toast.LENGTH_SHORT).show();
                connect();
            }

            @Override
            public void onFailure(int reasonCode) {
                Toast.makeText(MainActivity.this, "Discovery failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setWifiConnection(boolean isWifiOn) {

        this.isWifiOn = isWifiOn;
    }

    public void connect(){

        if(WiFiDirectBroadcastReceiver.peers.size() == 0) return;

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = WiFiDirectBroadcastReceiver.peers.get(0).deviceAddress;

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener(){

            @Override
            public void onFailure(int arg0) {
                System.out.println("correu mal");
            }

            @Override
            public void onSuccess() {
                System.out.println("correu bem");
            }

        });
    }
}
