package pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages;


public enum MessageType {
    PING, PONG,
    FIND_WORKSPACE, FIND_WORKSPACE_REPLY,
    INVITE, INVITE_REPLY,
    MY_WORKSPACES,
    FILES_MESSAGE, FILES_MESSAGE_REPLY
}