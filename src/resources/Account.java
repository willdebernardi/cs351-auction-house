package resources;

import java.nio.file.Paths;

public class Account {
    private String name;
    private int funds;

    public Account(String name, int funds) {
        this.name = name;
        this.funds = funds;
    }

    public String getName() {
        return name;
    }

    public int getFunds() {
        return funds;
    }
}
