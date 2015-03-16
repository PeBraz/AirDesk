package pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions;


public class ApplicationHasNoUserException extends Exception{
    private final String message = "Application has no user, login is required";
    public String getMessage() {
        return this.message;
    }
}
