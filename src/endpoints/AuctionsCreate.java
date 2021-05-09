package endpoints;

import resources.Auction;
import server.Request;
import server.Response;
import server.store.DataStore;
import server.store.Resource;

import javax.xml.crypto.Data;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.function.Function;

public class AuctionsCreate implements Function<Request, Response> {

    @Override
    public Response apply(Request request) {
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(request.getParameter(
                    "ip"
            ));
        } catch (UnknownHostException e) {
            return new Response(
                    "Unknown host.", null, Response.Type.ERROR
            );
        }

        int port = 0;
        int accountId = 0;
        try {
            port = Integer.parseInt(request.getParameter("port"));
            accountId = Integer.parseInt(request.getParameter("accountId"));
        } catch (NumberFormatException e) {
            return new Response(
                    "Invalid port.", null, Response.Type.ERROR
            );
        }

        Auction auction = new Auction(addr, port, accountId);
        Resource auctions = DataStore.getInstance().getResource("auctions");
        int id = auctions.create();
        auctions.putResource(id, auction);

        return new Response("", auction, Response.Type.OK);
    }
}
