package pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions;


public class WrongPasswordException extends Exception{

    private final String message = "Wrong password, please try again.";

    public String getMessage(){
        return this.message;
    }
}
