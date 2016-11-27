package aabramov.com.todomanager;

import aabramov.com.todomanager.model.ServerAddress;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Andrii Abramov on 11/27/16.
 */
@RunWith(Parameterized.class)
public class TestServerAddress {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"http", "192.168.0.1", 457, "http://192.168.0.1:457/"},
                {"https", "192.168.0.1", 457, "https://192.168.0.1:457/"},
                {"ftp", "192.168.0.1", 457, "ftp://192.168.0.1:457/"},
                {"smtp", "192.168.0.1", 457, "smtp://192.168.0.1:457/"},
                {"http", "example.com", 457, "http://example.com:457/"},
                {"https", "example.com", 457, "https://example.com:457/"},
                {"ftp", "example.com", 457, "ftp://example.com:457/"},
                {"smtp", "example.com", 457, "smtp://example.com:457/"}
        });
    }

    private String protocol;
    private String host;
    private int port;

    private String expectedAddress;

    public TestServerAddress(String protocol, String host, int port, String expectedAddress) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.expectedAddress = expectedAddress;
    }

    @Test
    public void testAddressFormation() {
        ServerAddress serverAddress = new ServerAddress(protocol, host, port);
        Assert.assertEquals(expectedAddress, serverAddress.getAsString());

    }

}