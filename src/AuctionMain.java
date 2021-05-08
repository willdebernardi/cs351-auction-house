import endpoints.Bid;
import endpoints.ItemView;
import endpoints.ItemsList;
import resources.Item;
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

        Resource items = new Resource<Item>("items", AuctionMain::generateItem);
        int randomNumber = new Random().nextInt(7);
        for(int i = 0; i < randomNumber; i++) {
            items.create();
        }
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

    public static Item generateItem() {
        FileInputStream nouns = null;
        FileInputStream adjectives = null;
        try {
            nouns = new FileInputStream(String.valueOf(
                    AuctionMain.class.getResource("/resources/nouns.txt")));
            adjectives = new FileInputStream(String.valueOf(
                    AuctionMain.class.getResource("/resources/adjectives.txt")));
        } catch (FileNotFoundException e) {
            System.out.println("world broke");
        }

        ArrayList<String> nounList = new ArrayList<>();
        ArrayList<String> adjectList = new ArrayList<>();
        Scanner nounScan = new Scanner(nouns);
        while(nounScan.hasNext()) {
            nounList.add(nounScan.nextLine());
        }
        Scanner adjectScan = new Scanner(adjectives);
        while(adjectScan.hasNext()) {
            adjectList.add(adjectScan.nextLine());
        }

        Collections.shuffle(nounList);
        Collections.shuffle(adjectList);
        String itemName = adjectList.get(0) + " " + nounList.get(0);
        return new Item(itemName, 0, -1, -1);
    }
}
