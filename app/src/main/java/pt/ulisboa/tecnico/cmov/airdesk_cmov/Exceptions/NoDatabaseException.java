package pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions;


public class NoDatabaseException extends RuntimeException{
    private final String message = "User database was not set, can't create user";
    public String getMessage() {
        return this.message;
    }
}
