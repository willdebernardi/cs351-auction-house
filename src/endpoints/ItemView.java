/**
 * Endpoint for retrieving an item by ID.
 *
 * @author Will Debernardi, Isaiah Martell, Christopher Medlin
 */
package endpoints;

import resource.Item;
import server.Request;
import server.Response;
import server.store.DataStore;
import server.store.Resource;

import java.util.function.Function;

public class ItemView implements Function<Request, Response> {
    @Override
    public Response apply(Request request) {
        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch(NumberFormatException e) {
            return new Response("Invalid numerical values",
                    null, Response.Type.ERROR);
        }

        Resource<Item> items = DataStore.getInstance().getResource("items");
        Item item = items.getResource(id);

        return new Response("", item, Response.Type.OK);
    }
}
