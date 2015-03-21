package pt.ulisboa.tecnico.cmov.airdesk_cmov;


public class File {


    private String name;
    private String path;
    private Workspace workspace;

    public File () {

    }


    public final String getName() {
        return name;
    }
    public final String getPath() {
        return path;
    }
    public final Workspace getWorkspace() {
        return workspace;
    }
    public final void setName(final String name) {
        this.name = name;
    }
    public final void setPath(final String path) {
        this.path = path;
    }
    public final void setWorkspace(final Workspace workspace) {
        this.workspace = workspace;
    }



}
