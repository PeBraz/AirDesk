package pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions;

/**
 *  Exception thrown when the Application owner tries to add himself to a list
 *
 * @see pt.ulisboa.tecnico.cmov.airdesk_cmov.Workspace
 */
public class UserIsMyselfException extends Exception{
    public String getMessage() {
        return "Can't add yourself.";
    }
}
