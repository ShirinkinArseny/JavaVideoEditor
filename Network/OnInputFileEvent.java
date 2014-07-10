package JVE.Network;

import java.io.File;

public interface OnInputFileEvent {

    public void run(Connection c, File f) throws Exception;

}
