package aabramov.com.todomanager.model;

import java.util.Objects;

/**
 * @author Andrii Abramov on 11/26/16.
 */

public class ServerAddress implements Cloneable {

    private final String protocol;
    private final String hostname;
    private final int port;

    public ServerAddress(String protocol, String hostname, int port) {
        this.protocol = protocol;
        this.hostname = hostname;
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getAsString() {
        return protocol + "://" + hostname + ":" + port + "/";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerAddress that = (ServerAddress) o;
        return port == that.port &&
                Objects.equals(protocol, that.protocol) &&
                Objects.equals(hostname, that.hostname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(protocol, hostname, port);
    }

    @Override
    public String toString() {
        return getAsString();
    }
}
