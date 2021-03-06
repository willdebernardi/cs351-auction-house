/**
 * Endpoint for blocking funds from an account.
 *
 * @author Will Debernardi, Isaiah Martell, Christopher Medlin
 */
package endpoints;

import resource.Account;
import server.Request;
import server.Response;
import server.store.DataStore;
import server.store.Resource;

import java.util.function.Function;

public class Block implements Function<Request, Response> {

    @Override
    public Response apply(Request request) {
        int id = 0;
        int funds = 0;
        try {
            id = Integer.parseInt(request.getParameter("id"));
            funds = Integer.parseInt(request.getParameter("funds"));
        } catch (NumberFormatException e) {
            return new Response("Invalid numerical values.",
                    null, Response.Type.ERROR);
        }

        Resource<Account> accounts = DataStore.getInstance()
                                              .getResource("accounts");
        Account a = accounts.getResource(id);

        a = a.block(funds);
        accounts.putResource(id, a);

        return new Response("", null, Response.Type.OK);
    }
}
