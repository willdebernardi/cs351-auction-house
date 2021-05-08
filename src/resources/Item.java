package resources;

public class Item {
    private String name;
    private int highestBid;
    private int bidderId;

    public Item(String name, int highestBid, int bidderId) {
        this.name = name;
        this.highestBid = highestBid;
        this.bidderId = bidderId;
    }

    public Item placeBid(int amount) {
        if (amount < this.highestBid) {
            throw new IllegalStateException("Bid too low.");
        }
        return new Item(this.name, amount, this.bidderId);
    }

    public String getName() {
        return name;
    }

    public int getHighestBidder() {
        return bidderId;
    }
}
