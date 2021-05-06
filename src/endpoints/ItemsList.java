package endpoints;

import server.Request;
import server.Response;
import server.store.DataStore;
import server.store.Resource;

import java.util.function.Function;

public class ItemsList implements Function<Request, Response> {

    @Override
    public Response apply(Request request) {
        Resource items = DataStore.getInstance().getResource("items");
        return new Response("", items.list(), Response.Type.OK);
    }
}
