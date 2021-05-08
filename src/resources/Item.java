package resources;

public class Item {
    private String name;
    private int highestBid;
    private int bidderId;
    private int winnerId;

    public Item(String name, int highestBid, int bidderId, int winnerId) {
        this.name = name;
        this.highestBid = highestBid;
        this.bidderId = bidderId;
        this.winnerId  = winnerId;
    }

    public Item placeBid(int amount) {
        if (amount < this.highestBid) {
            throw new IllegalStateException("Bid too low.");
        }
        return new Item(this.name, amount, this.bidderId, this.winnerId);
    }

    public Item newWinner(int winnerId) {
        return new Item(this.name, this.highestBid, this.bidderId, winnerId);
    }

    public String getName() {
        return name;
    }

    public int getHighestBid() {
        return highestBid;
    }
}
