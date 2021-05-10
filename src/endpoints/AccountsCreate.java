/**
 * Endpoint for account creation
 *
 * @author Will Debernardi, Isaiah Martell, Christopher Medlin
 */
package endpoints;

import resources.Account;
import server.Request;
import server.Response;
import server.store.DataStore;
import server.store.Resource;

import java.util.function.Function;

public class AccountsCreate implements Function<Request, Response> {
    @Override
    public Response apply(Request request) {
        DataStore ds = DataStore.getInstance();
        Resource accounts = ds.getResource("accounts");

        // attempt to convert funds from string to integer
        int funds = 0;
        try {
            funds = Integer.parseInt(request.getParameter("funds"));
        } catch (NumberFormatException e) {
            return new Response("Invalid initial funds.", null,
                                Response.Type.ERROR);
        }

        int id = accounts.create();
        accounts.putResource(id, new Account(
                request.getParameter("name"),
                funds, 0
        ));

        return new Response("", id, Response.Type.OK);
    }
}
