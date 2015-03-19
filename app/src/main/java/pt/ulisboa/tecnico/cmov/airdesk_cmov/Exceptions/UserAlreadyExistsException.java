package pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions;


public class UserAlreadyExistsException extends Exception{

    private final String message = "User already exists, please choose another username.";

    public String getMessage(){
        return this.message;
    }

}
