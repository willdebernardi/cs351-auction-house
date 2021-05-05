package resources;

import java.io.Serializable;
import java.nio.file.Paths;

public class Account implements Serializable {
    private String name;
    private int funds;
    private int blocked;

    public Account(String name, int funds, int blocked) {
        this.name = name;
        this.funds = funds;
        this.blocked = blocked;
    }

    public String getName() {
        return name;
    }

    public int getFunds() {
        return funds;
    }

    /**
     * Add funds to this account.
     *
     * @param funds the amount of funds to add.
     * @throws IllegalStateException if there are not enough funds to transfer
     */
    public Account addFunds(int funds) throws IllegalStateException {
        return new Account(this.name, this.funds+funds, this.blocked);
    }

    /**
     * Add funds to this account.
     *
     * @param funds the amount of funds to add.
     * @throws IllegalStateException if there are not enough funds to transfer
     */
    public Account removeFunds(int funds) throws IllegalStateException {
        if (this.funds - this.blocked < funds) {
            throw new IllegalStateException("Insufficient funds.");
        }
        return new Account(this.name, this.funds-funds, this.blocked);
    }

    /**
     * Blocks an amount of funds from this account.
     *
     * @param funds the amount to block
     * @throws IllegalStateException if there are not enough funds to block
     */
    public Account block(int funds) throws IllegalStateException {
        if (this.blocked + funds > this.funds) {
            throw new IllegalStateException("Insufficient funds.");
        }
        return new Account(this.name, this.funds, this.blocked+funds);
    }

    /**
     * Unblocks all blocked funds from the account.
     */
    public Account unblock() {
        return new Account(this.name, this.funds, 0);
    }
}
