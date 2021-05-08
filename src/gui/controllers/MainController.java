package gui.controllers;

import gui.ItemView;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import resources.Account;
import resources.Item;
import server.Client;
import server.Event;
import server.Request;
import server.Response;

import java.net.InetAddress;
import java.util.Set;

public class MainController {
    @FXML
    private VBox itemBox;
    @FXML
    private Label fundsLabel;

    private Client auctionClient;
    private Client bankClient;
    private int accountId;

    /**
     * Connects to the auction server at the given host and port number.
     */
    public void connectToAuction(InetAddress host, int port) {
        this.auctionClient = new Client(host, port);
        this.auctionClient.connect();
    }

    /**
     * Connects to the bank server at the given host and port number.
     */
    public void connectToBank(InetAddress host, int port) {
        this.bankClient = new Client(host, port);
        this.bankClient.connect();
    }

    /**
     * Refreshes the item list.
     */
    public void refresh() {
        itemBox.getChildren().removeAll();
        this.auctionClient.sendRequest(new Request("items.list"));
        Response response = this.auctionClient.waitForResponse();
        Set<Integer> itemIds = (Set<Integer>) response.getData();

        for (int id : itemIds) {
            itemBox.getChildren().add(new ItemView(id, accountId, auctionClient));
        }
    }

    /**
     * Connects the client in this controller to the given auction house server
     *
     * @param host host of the auction house server
     * @param port port of the auction house server
     */
    public void setAuctionHostAndPort(InetAddress host, int port) {
        this.auctionClient = new Client(host, port);
        this.auctionClient.setOnEvent(this::handleEvent);
    }

    // called when the client receives an event
    private void handleEvent(Event e) {
        if (e.getResourceName().equals("items")) {
            this.handleItemChanged(e.getChangedID());
        } else if (e.getResourceName().equals("accounts")) {
            // no need to pass ID, since the only account we're listening to is
            // our own.
            this.handleAccountChanged();
        }
    }

    private void handleItemChanged(int id) {
        this.auctionClient.sendRequest(new Request("items.get",
                "id", Integer.toString(id)));
        Response r = this.auctionClient.waitForResponse();
        Item item = (Item) r.getData();

        // if the highest bidder is not this agent
        if (item.getBidderId() != this.accountId) {
            this.auctionClient.sendRequest(new Request("accounts.get",
                    "id", Integer.toString(item.getBidderId())));
            Account bidder = (Account) this.auctionClient.waitForResponse().getData();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            String msg = String.format(
                    "You have been outbid by %s on %s",
                    bidder.getName(), item.getName()
            );
            alert.setContentText(msg);
            alert.show();
        }
    }

    private void handleAccountChanged() {
        this.bankClient.sendRequest(new Request("accounts.get",
                "id", Integer.toString(this.accountId)));
        Response r = this.bankClient.waitForResponse();

        int funds = ((Account) r.getData()).getFunds();
        this.fundsLabel.setText(Integer.toString(funds));
    }
}
