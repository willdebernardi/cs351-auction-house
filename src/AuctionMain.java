import endpoints.Bid;
import endpoints.ItemView;
import endpoints.ItemsList;
import resources.Item;
import server.*;
import server.store.DataStore;
import server.store.Resource;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AuctionMain {
    public static void main(String[] args) {
        Server server = new Server(44596);
        Client client = null;
        InetAddress address = null;
        int port = 0;
        if (args.length == 2) {
            try {
                address = InetAddress.getByName(args[0]);
                port = Integer.parseInt(args[1]);
                client = new Client(address,port);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        InetAddress serverAddress = server.getAddress();
        String hostAddress = serverAddress.getHostAddress();
        String hostPort = String.valueOf(server.getPort());
        client.connect();
        client.sendRequest(new Request("auctions.create",
                "ip", hostAddress, "port", hostPort));
        Response r = client.waitForResponse();

        Resource items = new Resource<Item>("items",
                () -> new Item("",0 ,-1, -1));
        Resource auctionAccountId = new Resource<Integer>("auctionId",
                () -> 0);
        int id = auctionAccountId.create();
        auctionAccountId.putResource(id, r.getData());

        DataStore.instantiate(items);

        server.addEndpoint(new Endpoint("items.get",
                new ItemView(), "id"));
        server.addEndpoint(new Endpoint("items.list",
                new ItemsList()));
        server.addEndpoint(new Endpoint("items.bid",
                new Bid(address, port)));
    }
}
