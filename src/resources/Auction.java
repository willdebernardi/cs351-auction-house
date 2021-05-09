package resources;

import java.io.Serializable;
import java.net.InetAddress;

public class Auction implements Serializable {
    private InetAddress ip;
    private int port;
    private int accountId;

    /**
     * Represents the IP and the port of an auction house server.
     *
     * @param ip the ip of the auction house server
     * @param port the port of the auction house server
     */
    public Auction(InetAddress ip, int port, int accountId) {
        this.ip = ip;
        this.port = port;
        this.accountId = accountId;
    }

    /**
     * Returns the ip of the auction house server of this auction house.
     *
     * @return ip
     */
    public InetAddress getIp() {
        return ip;
    }

    /**
     * Returns the port of the auction house server of this auction house.
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Returns the account id associated with this auction house.
     */
    public int getAccountId() {
        return accountId;
    }

    @Override
    public String toString() {
        return ip.toString();
    }
}