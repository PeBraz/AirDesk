package pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions;


public class UserAlreadyExistsException extends Exception{

    private final String message = "User not found, please sign up.";

    public String getMessage(){
        return this.message;
    }

}
