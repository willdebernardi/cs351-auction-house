/**
 * Ran when the auction server shuts down in order to deregister the auction
 * house from the bank
 *
 * @author Will Debernardi, Isaiah Martell, Christopher Medlin
 */
import server.Client;
import server.Request;

public class ShutdownHook implements Runnable {
    private Client client;
    private int auctionId;

    public ShutdownHook(Client c, int auctionId) {
        this.client = c;
        this.auctionId = auctionId;
    }

    @Override
    public void run() {
        this.client.sendRequest(new Request("auctions.deregister",
            "id", Integer.toString(auctionId)
        ));
    }
}
