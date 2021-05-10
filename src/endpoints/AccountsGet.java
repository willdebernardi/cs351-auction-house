/**
 * Endpoint for retrieving an account by Id.
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

public class AccountsGet implements Function<Request, Response> {

    @Override
    public Response apply(Request r) {
        int id = 0;
        try {
            id = Integer.parseInt(r.getParameter("id"));
        } catch (NumberFormatException e) {
            return new Response("Invalid ID", null, Response.Type.ERROR);
        }

        Resource<Account> accounts = DataStore.getInstance()
                                              .getResource("accounts");
        Account a = accounts.getResource(id);
        return new Response("", a, Response.Type.OK);
    }
}
