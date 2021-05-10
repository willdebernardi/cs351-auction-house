/**
 * Endpoint for deleting an account.
 *
 * @author Will Debernardi, Isaiah Martell, Christopher Medlin
 */
package endpoints;

import resources.Auction;
import server.Request;
import server.Response;
import server.store.DataStore;
import server.store.Resource;

import java.util.function.Function;

public class AuctionRemove implements Function<Request, Response> {

    @Override
    public Response apply(Request r) {
        Resource<Auction> auctions = DataStore.getInstance().getResource(
                "auctions"
        );

        int id = 0;
        try {
            id = Integer.parseInt(r.getParameter("id"));
        } catch (NumberFormatException e) {
            return new Response("", null, Response.Type.ERROR);
        }

        auctions.removeResource(id);
        return new Response("", null, Response.Type.OK);
    }
}
