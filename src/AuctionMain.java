import endpoints.ItemView;
import endpoints.ItemsList;
import resources.Item;
import server.Client;
import server.Endpoint;
import server.Request;
import server.Server;
import server.store.DataStore;
import server.store.Resource;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AuctionMain {
    public static void main(String[] args) {
        Server server = new Server(44596);
        Client client = null;
        if (args.length == 2) {
            try {
                InetAddress address = InetAddress.getByName(args[0]);
                int port = Integer.parseInt(args[1]);
                client = new Client(address,port);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        InetAddress address = server.getAddress();
        String hostAddress = address.getHostAddress();
        String hostPort = String.valueOf(server.getPort());
        client.connect();
        client.sendRequest(new Request("auctions.create",
                hostAddress, hostPort));

        Resource items = new Resource<Item>("items",
                () -> new Item("",0 ,-1));

        DataStore.instantiate(items);

        server.addEndpoint(new Endpoint("items.get",
                new ItemView(), "id"));
        server.addEndpoint(new Endpoint("items.list",
                new ItemsList()));
    }
}
