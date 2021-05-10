/**
 * Removes all fund blockages from an account.
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

public class Unblock implements Function<Request, Response> {

    @Override
    public Response apply(Request request) {
        int id = 0;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            return new Response("Invalid numerical values.",
                    null, Response.Type.ERROR);
        }

        Resource<Account> accounts = DataStore.getInstance()
                                              .getResource("accounts");
        Account a = accounts.getResource(id);

        a = a.unblock();
        accounts.putResource(id, a);

        return new Response("", null, Response.Type.OK);
    }
}
