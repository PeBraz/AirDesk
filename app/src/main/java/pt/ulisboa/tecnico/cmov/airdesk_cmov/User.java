package pt.ulisboa.tecnico.cmov.airdesk_cmov;

public class User {

    private static long id;
    private String username;


    private String email;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(){

    }

    public User(String username){
        this.username = username;
    }

    public static long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return username;
    }
}
