import endpoints.AccountsCreate;
import endpoints.AuctionsCreate;
import endpoints.AuctionsList;
import endpoints.Block;
import endpoints.Transfer;
import endpoints.Unblock;
import resources.Account;
import resources.Auction;
import server.Endpoint;
import server.Server;
import server.store.DataStore;
import server.store.Resource;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class BankMain {
    public static void main(String[] args) {
        Resource accounts = new Resource<Account>("accounts",
                () -> new Account("", 0, 0));

        Resource auctions = null;
        try {
            final InetAddress localhost = InetAddress.getLocalHost();
            auctions = new Resource<Auction>("auctions",
                    () -> new Auction(localhost, 5398));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        DataStore.instantiate(accounts, auctions);

        Server server = new Server(37281);

        server.addEndpoint(new Endpoint("accounts.create",
                new AccountsCreate(), "name", "funds"));
        server.addEndpoint(new Endpoint("auctions.create",
                new AuctionsCreate(), "ip", "port"));
        server.addEndpoint(new Endpoint("auctions.list",
                new AuctionsList()));
        server.addEndpoint(new Endpoint("accounts.transfer",
                new Transfer(), "id1", "id2", "funds"));
        server.addEndpoint(new Endpoint("accounts.block",
                new Block(), "id", "funds"));
        server.addEndpoint(new Endpoint("accounts.unblock",
                new Unblock(), "id"));

        server.run();
    }
}
