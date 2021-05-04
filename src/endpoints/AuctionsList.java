package endpoints;

import server.Request;
import server.Response;
import server.store.DataStore;
import server.store.Resource;

import java.util.function.Function;

public class AuctionsList implements Function<Request, Response> {
    @Override
    public Response apply(Request request) {
        Resource auctions = DataStore.getInstance().getResource("auctions");
        return new Response("", auctions.list(), Response.Type.OK);
    }
}
