/**
 * TimerTask that checks if a higher bid has been placed on an item since the
 * time the timer task was constructed. If there has been no higher bid, we set
 * a winner, thus firing events to all listeners of the item, and wait for the
 * appropriate amount of funds to be transferred to the auction house's bank
 * account.
 *
 * @author Will Debernardi, Isaiah Martell, Christopher Medlin
 */
package endpoints;

import resource.Item;
import server.Client;
import server.Event;
import server.Request;
import server.store.DataStore;
import server.store.Resource;

import java.util.TimerTask;

public class ItemTimer extends TimerTask {
    private int itemId;
    private int accountId;
    private int funds;
    private Client client;

    public ItemTimer(int itemId, int funds, int accountId, Client client) {
        this.itemId = itemId;
        this.funds = funds;
        this.accountId = accountId;
        this.client = client;
    }
    @Override
    public void run() {
        Resource<Item> items = DataStore.getInstance().getResource("items");
        // retrieve auction house's bank account id
        int auctionId = (int) DataStore.getInstance()
                                       .getResource("auctionId")
                                       .getResource(1);
        Item item = items.getResource(itemId);
        if(funds == item.getHighestBid()) {
            client.setOnEvent(this::handleEvent);
            client.sendRequest(new Request(
                    "listen",
                    "url", "accounts." + Integer.toString(auctionId))
            );
            client.sendRequest(new Request(
                    "accounts.unblock",
                    "id", String.valueOf(accountId)));
            client.waitForResponse();
            item = item.newWinner(accountId);
            items.putResource(itemId, item);
        }
    }

    public void handleEvent(Event event) {
        Resource<Item> items = DataStore.getInstance().getResource("items");
        items.removeResource(itemId);
        items.create();
    }
}
