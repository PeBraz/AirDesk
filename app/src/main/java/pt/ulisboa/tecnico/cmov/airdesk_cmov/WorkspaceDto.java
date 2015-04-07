package pt.ulisboa.tecnico.cmov.airdesk_cmov;

import java.io.Serializable;

/**
 * WorkspaceDto for database storage
 *
 */
public class WorkspaceDto implements Serializable {
    private final String userEmail;
    private final String wsName;
    public WorkspaceDto (String userEmail, String wsName){
        this.userEmail = userEmail;
        this.wsName = wsName;
    }
    public final String getUserEmail() {
        return this.userEmail;
    }
    public final String getWSName() {
        return this.wsName;
    }
    @Override
    public final boolean equals(Object o) {
        return this.getUserEmail().equals(((WorkspaceDto) o).getUserEmail())
                && this.getWSName().equals(((WorkspaceDto)o).getWSName());
    }

    @Override
    public final String toString(){
        return this.getWSName() + " - " + this.getUserEmail();
    }

}
