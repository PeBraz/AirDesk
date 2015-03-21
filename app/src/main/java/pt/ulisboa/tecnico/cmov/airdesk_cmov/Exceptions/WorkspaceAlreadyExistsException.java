package pt.ulisboa.tecnico.cmov.airdesk_cmov.Exceptions;


public final class WorkspaceAlreadyExistsException extends Exception {
    final private String name;
    public WorkspaceAlreadyExistsException(final String name) {
        this.name = name;
    }
    public final String getMessage() {
        return "Workspace with name " + this.name + " already exists.";
    }
}
