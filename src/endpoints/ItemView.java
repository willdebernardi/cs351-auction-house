package endpoints;

import resources.Item;
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

        return new Response("Item requested: " + item.getName(), null, Response.Type.OK);
    }
}
