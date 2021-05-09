package resources;

import java.io.Serializable;

public class Item implements Serializable {
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

    public Item placeBid(int amount, int bidderId) {
        if (amount < this.highestBid) {
            throw new IllegalStateException("Bid too low.");
        }
        return new Item(this.name, amount, bidderId, this.winnerId);
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

    public int getBidderId() {
        return bidderId;
    }

    public int getWinnerId() {
        return winnerId;
    }
}
