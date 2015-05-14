package pt.ulisboa.tecnico.cmov.airdesk_cmov;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.FilesDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.UsersDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.WorkspacesDataSource;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.ApplicationHasNoUserException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NoDatabaseException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.NotRegisteredException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.UserAlreadyExistsException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.UserNotFoundException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions.WrongPasswordException;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.Peer;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.ServerThread;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.FindWorkspaceReplyMessage;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Network.messages.Message;
import pt.ulisboa.tecnico.cmov.airdesk_cmov.Sessions.SessionManager;


public class Application {


    public static SessionManager session = null;

    private static ApplicationOwner owner = null;

    private static UsersDataSource userData = null;

    private static FilesDataSource fileData = null;

    public static List<Map<String,String[]>> networkWorkspaces = null;

    private static Map<String,Peer> peers = new HashMap<>();

    //The Data source should not be called from here, should instead be called from the owner
    private static WorkspacesDataSource workspaceData = null;

    //Application quota int bytes
    private static final int MAX_APPLICATION_QUOTA = 20;

    public static final Set<WorkspaceDto> foreignWorkspaces = new HashSet<>();

    public static void init(android.content.Context AppContext) {
        Application.setUsersDataSource(new UsersDataSource(AppContext));
        Application.setWorkspacesDataSource(new WorkspacesDataSource(AppContext));
        Application.setFileDataSource(new FilesDataSource(AppContext));
        Application.session = new SessionManager(AppContext);
    }


    public static void createUser(String username, String email)
        throws NoDatabaseException, UserAlreadyExistsException {

        if (Application.userData == null) throw new NoDatabaseException();

        User u = new User(username, email);
        boolean isAllowed = checkUser(u);

        if (!isAllowed) userData.create(u);

        else throw new UserAlreadyExistsException();
    }

    public static void setFileDataSource(FilesDataSource filedb) {Application.fileData = filedb;}
    public static void setUsersDataSource(UsersDataSource userdb) {
        Application.userData = userdb;
    }
    public static void setWorkspacesDataSource(WorkspacesDataSource workspacedb) {
        Application.workspaceData = workspacedb;
    }

    public static ApplicationOwner getOwner() {
        if (Application.owner == null)
            throw new ApplicationHasNoUserException();

        return Application.owner;
    }

    public Application (User u) {
        Application.owner = ApplicationOwner.fromUser(u);
    }

    private static boolean checkUser(User u){

        User user = userData.get(u.getEmail());
        return user != null;
    }

    public static void login(String email)
          throws NotRegisteredException, NoDatabaseException, WrongPasswordException{

        if (userData == null) throw new NoDatabaseException();

        User u = userData.get(email);
        if (u == null)throw new NotRegisteredException();

        Application.owner = ApplicationOwner.fromUser(u);

    }

    public static User getUser(String email){
        return userData.get(email);
    }


    /**
     * Tries to find a user in the network with a specific email
     *
     * @param email unique identifier for the user in the local network
     */
    public static User getNetworkUser(String email) throws UserNotFoundException{
        for (User u : Application.userData.getAll())
            if (u.getEmail().equals(email))
                return u;
        throw new UserNotFoundException(email);
    }

    /**
     * Local method to simulate the workspaces that are visible (public) in the network
     *
     * @return all public workspaces
     */

    public static Set<Workspace> getPublicNetworkWS() {
        final Set<Workspace> publicWS = new HashSet<>();
        for (Workspace ws : Application.workspaceData.getAll()) {
            if (!ws.getPrivacy())
                publicWS.add(ws);
        }
        return publicWS;
    }

    /**
     * Method used to simulate all workspaces in the network, users have access to private workspaces
     * if they were invited or subscribed while public
     *
     * @return all workspaces in the current network
     */
    public static Set<Workspace> getAllNetworkWS() {
        return  new HashSet<>(Application.workspaceData.getAll());
    }


    /**
     * Will search for workspaces available in the network
     *  (don't confuse workspacesInNetwork with foreignWorkspace)
     *
     */

    public static Set<WorkspaceDto> networkSearch(String query) throws IOException {

        String[] queryArr = query.split("\\s+");
        Set<WorkspaceDto> availableWS = new HashSet<>();

/*
        List<WorkspaceDto> allPublicWorkspaces = new ArrayList<>();

        //By default providing no arguments to the query will show all available workspaces
        if (query.trim().isEmpty())
            return new HashSet<>(allPublicWorkspaces);
*/

        for(Peer p : peers.values()) {
            for (Map.Entry<String, String[]> workspaces : p.getWorkspaces().entrySet()) {
                if (query.trim().isEmpty())
                    availableWS.add(new WorkspaceDto(p.getOwner(), workspaces.getKey()));
                else {
                    for (String s : workspaces.getValue()) {
                        for (String tag : queryArr) {
                            if (s.equals(tag)) {
                                availableWS.add(new WorkspaceDto(p.getOwner(), workspaces.getKey()));
                            }
                        }
                    }
                }
            }
        }

        return availableWS;
    }

    public static Peer getPeer(String email){

        if(peers.containsKey(email)){
            return peers.get(email);
        }
/*
        else{
            Peer p = new Peer(email);
            peers.put(email,p);
            return p;
        }*/
        return null;
    }

    public static boolean hasPeer(String email){
        return Application.getPeer(email) != null;
    }

    public static void subscribe(WorkspaceDto ws){
        //Application.foreignWorkspaces.add(ws);
        Application.owner.addForeign(ws.getUserEmail(), ws.getWSName());
    }

    public static void subscribe(Set<WorkspaceDto> targetWs) {
        //Application.foreignWorkspaces.addAll(targetWs);
        for (WorkspaceDto ws : targetWs){
            Application.owner.addForeign(ws.getUserEmail(), ws.getWSName());
        }
    }

    /**
    * Returns the device storage available in the internal storage of the device (as bytes)
    *
    *  @return space available in the device as bytes
    */
    public static int getDeviceStorageSpace() {
        return  Application.MAX_APPLICATION_QUOTA - getReservedStorage();
        // StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        //return (int) ((long)stat.getBlockSize() * (long)stat.getBlockCount());
    }

    /**
     * Calculates the total quota reserved being used in the device by all workspaces,
     * even from other local users (as bytes).
     *
     * @return storage reserved by the application in bytes
     */
    public static int getReservedStorage() {
        int totalStorage = 0;
        for (Workspace ws: Application.workspaceData.getAll()) {
            totalStorage += ws.getQuota();
        }
        return totalStorage;
    }
    /**
     * Saves a modified user in the application
     * @param u user to be changed
     */
    public static void saveUser(User u) {
        Application.userData.save(u);
    }

    public static void remove(Workspace ws){
        Application.workspaceData.remove(ws.getName(), ws.populateUser().getOwner().getEmail());
    }

    public static void createPeer(String email, Socket s){
        if (!Application.peers.containsKey(email)) {
            Application.peers.put(email, new Peer(email,s));
        }
    }

    public static List<Peer> getPeers(){
        return new ArrayList<>(peers.values());
    }


    public static Set<WorkspaceDto> getForeign(){
        Set<WorkspaceDto> returnDtos = new HashSet<>();
        System.out.println("Im subscribed to:");
        for (WorkspaceDto dto : Application.getOwner().getForeign()){   //all my foreign workspaces
            System.out.print(dto.getWSName());
            if (Application.hasPeer(dto.getUserEmail())) {              // if is connected on the network
                System.out.print(": Is connected");
                Peer peer = Application.getPeer(dto.getUserEmail());
                if (peer.workspaceExists(dto.getWSName())) {        // if the owner broadcasted the workspace
                    System.out.println(": The owner has it");
                    returnDtos.add(dto);
                } else {
                    System.out.println(": The owner does not have it");
                    Application.getOwner().remForeign(dto.getUserEmail(), dto.getWSName());
                }
            }
        }
        return returnDtos;
    //    return Application.foreignWorkspaces;
    }

    public static void removePeer(Peer peer){
        System.out.println("Removed peer: " + peer.getOwner());
        Application.peers.remove(peer.getOwner());
    }
}
