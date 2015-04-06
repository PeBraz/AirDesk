package pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions;


public class FileNotFoundException extends Exception {

    private final String message = "File is still empty.";
    public String getMessage() {
        return this.message;
    }
}
