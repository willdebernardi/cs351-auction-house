package endpoints;

import resources.Item;
import server.store.DataStore;
import server.store.Resource;

import java.util.TimerTask;

public class ItemTimer extends TimerTask {
    private int itemId;
    private int accountId;
    private int funds;

    public ItemTimer(int itemId, int funds, int accountId) {
        this.itemId = itemId;
        this.funds = funds;
        this.accountId = accountId;
    }
    @Override
    public void run() {
        Resource<Item> items = DataStore.getInstance().getResource("items");
        Item item = items.getResource(itemId);
        if(funds == item.getHighestBid()) {
            item = item.newWinner(accountId);
            items.putResource(itemId, item);
        }
    }
}
