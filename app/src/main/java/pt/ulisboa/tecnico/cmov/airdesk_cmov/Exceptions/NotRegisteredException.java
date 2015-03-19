package pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions;


public class NotRegisteredException extends Exception{

    private final String message = "Your are not registered yet, please sign up.";

    public String getMessage(){
        return this.message;
    }
}

