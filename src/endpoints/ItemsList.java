package endpoints;

import resources.Item;
import server.Request;
import server.Response;
import server.store.DataStore;
import server.store.Resource;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ItemsList implements Function<Request, Response> {

    @Override
    public Response apply(Request request) {
        Resource<Item> items = DataStore.getInstance().getResource("items");
        Set<Integer> ids = new HashSet<>(items.listIds());
        return new Response("", ids, Response.Type.OK);
    }
}
