package pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions;

public final class UserNotFoundException extends Exception {
    private final String email;
    public UserNotFoundException(final String email) {
        this.email = email;
    }
    @Override
    public String getMessage() {
        return "No user was found with email: " + this.email + ".";
    }
}
