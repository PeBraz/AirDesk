package pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions;

/**
 * Exception thrown when a user tries to perform an action over an object that he doesn't own
 *
 */
public final class NotOwnerException extends RuntimeException {
    @Override
    public final String getMessage() {
        return "User tried to perform an action on a object he doesn't own";
    }
}
