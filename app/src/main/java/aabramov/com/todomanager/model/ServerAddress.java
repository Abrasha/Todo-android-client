package aabramov.com.todomanager.model;

/**
 * @author Andrii Abramov on 11/26/16.
 */

public class ServerAddress implements Cloneable {

    private final String protocol;
    private final String host;
    private final int port;

    public ServerAddress(String protocol, String host, int port) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getAsString(){
        return protocol + "://" + host + ":" + port + "/";
    }

}
