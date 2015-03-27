package pt.ulisboa.tecnico.cmov.airdesk_cmov;


import pt.ulisboa.tecnico.cmov.airdesk_cmov.Database.FilesDataSource;


public class File {

    private String name;
    private String path;
    private String workspace;

    public File () {


    }

    public File(String name, String workspace) {
        this.name = name;
        this.workspace = workspace;
    }

    public final String getName() {
        return name;
    }
    public final String getPath() {
        return path;
    }
    public final String getWorkspace() {
        return workspace;
    }
    public final void setName(final String name) {
        this.name = name;
    }
    public final void setPath(final String path) {
        this.path = path;
    }
    public final void setWorkspace(final String workspace) {
        this.workspace = workspace;
    }



}
