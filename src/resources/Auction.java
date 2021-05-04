package resources;

import java.net.InetAddress;

public class Auction {
    private InetAddress ip;
    private int port;

    /**
     * Represents the IP and the port of an auction house server.
     *
     * @param ip the ip of the auction house server
     * @param port the port of the auction house server
     */
    public Auction(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
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
}