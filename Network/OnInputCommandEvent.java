package JVE.Network;

public interface OnInputCommandEvent {

    public void run(Connection c, String message) throws Exception;

}
