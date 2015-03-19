package pt.ulisboa.tecnico.cmov.airdesk_cmov;

/**
 * Created by pedro on 19-03-2015.
 */
public class File {


    private String name;
    private String path;
    private String workspace;

    public File () {

    }


    public String getName() {
        return name;
    }
    public String getPath() {
        return path;
    }
    public String getWorkspace() {
        return workspace;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }



}
