package JVE.Network;

public interface ConnectionEvent {

    public void run(Connection c, String message) throws Exception;

}
