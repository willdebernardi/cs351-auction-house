/**
 * Main class for the Auction server.
 *
 * @author Will Debernardi, Isaiah Martell, Christopher Medlin
 */
import endpoints.Bid;
import endpoints.ItemView;
import endpoints.ItemsList;
import resource.Item;
import server.*;
import server.store.DataStore;
import server.store.Resource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class AuctionMain {
    public static void main(String[] args) {
        Client client = null;
        InetAddress address = null;
        InetAddress serverAddress = null;
        int port = 0;
        int serverPort = 0;
        if (args.length == 4) {
            try {
                address = InetAddress.getByName(args[0]);
                port = Integer.parseInt(args[1]);
                client = new Client(address,port);
                serverAddress = InetAddress.getByName(args[2]);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                System.exit(1);
            }
            serverPort = Integer.parseInt(args[3]);
        }

        Server server = new Server(serverPort);
        String hostAddress = serverAddress.getHostAddress();
        String hostPort = String.valueOf(server.getPort());
        client.connect();
        client.sendRequest(new Request("accounts.create",
                "name", "auction", "funds", "0"));
        int accountId = (int) client.waitForResponse().getData();
        client.sendRequest(new Request("auctions.create",
                "ip", hostAddress, "port", hostPort,
                "accountId", Integer.toString(accountId)));
        int auctionId = (int) client.waitForResponse().getData();

        Resource items = new Resource<Item>("items", AuctionMain::generateItem);
        int randomNumber = new Random().nextInt(3) + 5;
        for(int i = 0; i < randomNumber; i++) {
            items.create();
        }
        Resource auctionAccountId = new Resource<Integer>("auctionId",
                () -> 0);
        int id = auctionAccountId.create();
        auctionAccountId.putResource(id, accountId);

        DataStore.instantiate(items, auctionAccountId);

        server.addEndpoint(new Endpoint("items.get",
                new ItemView(), "id"));
        server.addEndpoint(new Endpoint("items.list",
                new ItemsList()));
        server.addEndpoint(new Endpoint("items.bid",
                new Bid(address, port)));

        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook(
                client, auctionId
        )));
        server.run();
    }

    public static Item generateItem() {
        Scanner nounScan = null;
        Scanner adjectScan = null;
        try {
            nounScan = new Scanner(AuctionMain.class.getResourceAsStream(
                    "/resources/nouns.txt"
            ));
            adjectScan = new Scanner(AuctionMain.class.getResourceAsStream(
                    "/resources/adjectives.txt"
            ));
        } catch (Exception e) {
            System.out.println("world broke");
        }

        ArrayList<String> nounList = new ArrayList<>();
        ArrayList<String> adjectList = new ArrayList<>();
        while(nounScan.hasNext()) {
            nounList.add(nounScan.nextLine());
        }
        while(adjectScan.hasNext()) {
            adjectList.add(adjectScan.nextLine());
        }

        Collections.shuffle(nounList);
        Collections.shuffle(adjectList);
        String itemName = adjectList.get(0) + " " + nounList.get(0);
        return new Item(itemName, 0, -1, -1);
    }
}
