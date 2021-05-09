package endpoints;

import resources.Account;
import resources.Item;
import server.Client;
import server.Request;
import server.Response;
import server.store.DataStore;
import server.store.Resource;

import java.net.InetAddress;
import java.util.Timer;
import java.util.function.Function;

public class Bid implements Function<Request, Response> {
    private InetAddress bankAddress;
    private int port;
    public Bid(InetAddress bankAddress, int port) {
        this.bankAddress = bankAddress;
        this.port = port;
    }
    @Override
    public Response apply(Request request) {
        Timer timer = new Timer();
        Client client = new Client(bankAddress, port);
        client.connect();
        int itemId = 0;
        int accountId= 0;
        int funds = 0;

        try {
            itemId = Integer.parseInt(request.getParameter("id"));
            accountId = Integer.parseInt(request.getParameter("accountId"));
            funds = Integer.parseInt(request.getParameter("funds"));
        } catch (NumberFormatException e) {
            return new Response("Invalid numerical values.",
                    null, Response.Type.ERROR);
        }

        Resource<Item> items = DataStore.getInstance().getResource("items");
        Item i = items.getResource(itemId);

        if(funds <= i.getHighestBid()) {
            return new Response("Bid amount too low",
                    null, Response.Type.ERROR);
        }

        if (accountId == i.getBidderId()) {
            return new Response("You can't outbid yourself.",
                    null, Response.Type.ERROR);
        }

        client.sendRequest(new Request("accounts.block",
        "funds", Integer.toString(funds),
                    "id", Integer.toString(accountId)
        ));

        Response r = client.waitForResponse();
        // if there is an error, relay the error response back to the agent
        if (r.getType() == Response.Type.ERROR) {
            return r;
        }

        int bidderId = i.getBidderId();
        i = i.placeBid(funds, accountId);
        items.putResource(itemId, i);
        client.sendRequest(new Request("accounts.unblock",
                "id", String.valueOf(bidderId)));
        timer.schedule(new ItemTimer(itemId, funds, accountId, client), 30000);

        return new Response("", null, Response.Type.OK);
    }
}
