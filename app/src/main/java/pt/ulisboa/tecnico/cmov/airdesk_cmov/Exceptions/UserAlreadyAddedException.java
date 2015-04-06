package pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions;

/**
 * Exception thrown when a used already exists in a list
 *
 * @see pt.ulisboa.tecnico.cmov.airdesk_cmov.Workspace
 */
public final class UserAlreadyAddedException extends Exception{
    private final String email;
    public UserAlreadyAddedException(String email) {
        this.email = email;
    }
    @Override
    public final String getMessage() {
        return "User with email: " + this.email+ " was already added.";
    }
}
