package resources;

import java.nio.file.Paths;

public class Account {
    private String name;
    private int funds;
    private int blocked;

    public Account(String name, int funds) {
        this.name = name;
        this.funds = funds;
        this.blocked = 0;
    }

    public String getName() {
        return name;
    }

    public int getFunds() {
        return funds;
    }

    /**
     * Transfers an amount of funds from this account to another.
     *
     * @param a     the account to transfer to
     * @param funds the amount of funds to transfer
     * @throws IllegalStateException if there are not enough funds to transfer
     */
    public void transfer(Account a, int funds) throws IllegalStateException {
        if (this.funds - this.blocked < funds) {
            throw new IllegalStateException("Insufficient funds.");
        }
        this.funds -= funds;
        a.funds += funds;
    }

    /**
     * Blocks an amount of funds from this account.
     *
     * @param funds the amount to block
     * @throws IllegalStateException if there are not enough funds to block
     */
    public void block(int funds) throws IllegalStateException {
        if (this.blocked + funds > this.funds) {
            throw new IllegalStateException("Insufficient funds.");
        }
        this.blocked += funds;
    }

    /**
     * Unblocks all blocked funds from the account.
     */
    public void unblock() {
        this.blocked = 0;
    }
}
