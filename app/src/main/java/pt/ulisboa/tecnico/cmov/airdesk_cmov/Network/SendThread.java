package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.airdesk_cmov.Application;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.MyWorkspacesMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Workspace;

class SendThread extends Thread {
    final int FIVE_SECOND = 5000;

    private Map<String, String[]> workspaces = new HashMap<>();

    public SendThread() {
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(FIVE_SECOND);
                checkWorkspaces();
                boolean success;
                for (Peer peer : Application.getPeers()) {
                    success = peer.send(new MyWorkspacesMessage(workspaces,
                                Application.getOwner().getEmail()));
                    if (!success)
                        Application.removePeer(peer);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void checkWorkspaces() {
        for (Workspace ws : Application.getOwner().getMyWorkspaces()) {
            workspaces.put(ws.getName(), ws.getTagsAsArray());
        }
    }
}
