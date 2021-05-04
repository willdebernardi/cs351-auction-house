import endpoints.AccountsCreate;
import resources.Account;
import server.Endpoint;
import server.Server;
import server.store.DataStore;
import server.store.Resource;

import javax.xml.crypto.Data;
import java.util.HashSet;

public class BankMain {
    public static void main(String[] args) {
        DataStore.instantiate(new Resource<Account>("accounts",
                () -> new Account("", 0, 0)));

        Server server = new Server(37281);

        server.addEndpoint(new Endpoint("accounts.create",
                new AccountsCreate(), "name", "funds"));

        server.run();
    }
}
