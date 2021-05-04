import com.sun.jdi.connect.LaunchingConnector;
import endpoints.AccountsCreate;
import resources.Account;
import resources.Auction;
import server.Endpoint;
import server.Server;
import server.store.DataStore;
import server.store.Resource;

import javax.xml.crypto.Data;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;

public class BankMain {
    public static void main(String[] args) {
        Resource accounts = new Resource<Account>("accounts",
                () -> new Account("", 0));

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

        server.run();
    }
}
