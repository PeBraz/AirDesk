package pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions;


public final class InvalidQuotaException extends Exception {
    final private int quota;
    public InvalidQuotaException(final int quota) {
        this.quota = quota;
    }
    public final String getMessage() {
        return "Quota value of " + this.quota + " is too high.";
    }
}
