package endpoints;

import resources.Account;
import server.Request;
import server.Response;
import server.store.DataStore;
import server.store.Resource;

import java.util.function.Function;

public class Transfer implements Function<Request, Response> {
    @Override
    public Response apply(Request request) {
        int id1 = 0;
        int id2 = 0;
        int funds = 0;
        try {
            id1 = Integer.parseInt(request.getParameter("id1"));
            id2 = Integer.parseInt(request.getParameter("id2"));
            funds = Integer.parseInt(request.getParameter("funds"));
        } catch (NumberFormatException e) {
            return new Response("Invalid numerical values.",
                                null, Response.Type.ERROR);
        }

        Resource<Account> accounts = DataStore.getInstance()
                                              .getResource("accounts");
        Account a1 = (Account) accounts.getResource(id1);
        Account a2 = (Account) accounts.getResource(id1);

        try {
            a1 = a1.removeFunds(funds);
            a2 = a2.addFunds(funds);
        } catch (IllegalStateException e) {
            return new Response(
                    "Not enough funds.", null, Response.Type.ERROR
            );
        }

        accounts.putResource(id1, a1);
        accounts.putResource(id2, a2);

        return new Response("", null, Response.Type.OK);
    }
}
