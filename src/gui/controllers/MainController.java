package gui.controllers;

import endpoints.Bid;
import gui.ItemView;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import resources.Account;
import resources.Item;
import server.Client;
import server.Event;
import server.Request;
import server.Response;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MainController {
    @FXML
    private VBox itemBox;
    @FXML
    private Label fundsLabel;

    private Client auctionClient;
    private Client bankClient;
    private int accountId;
    private int auctionAccountId;

    /**
     * Connects to the auction server at the given host and port number.
     */
    public void connectToAuction(InetAddress host, int port) {
        this.auctionClient = new Client(host, port);
        this.auctionClient.setOnEvent(this::handleEvent);
        this.auctionClient.connect();
    }

    /**
     * Connects to the bank server at the given host and port number.
     */
    public void connectToBank(InetAddress host, int port) {
        this.bankClient = new Client(host, port);
        this.bankClient.setOnEvent(this::handleEvent);
        this.bankClient.connect();
        this.bankClient.sendRequest(new Request("listen",
                "url", "accounts." + accountId));
    }

    /**
     * Refreshes the item list.
     */
    public void refresh() {
        itemBox.getChildren().clear();
        this.auctionClient.sendRequest(new Request("items.list"));
        Response response = this.auctionClient.waitForResponse();
        Set<Integer> itemIds = (Set<Integer>) response.getData();

        for (int id : itemIds) {
            ItemView iv = new ItemView(id, accountId, auctionClient);
            iv.setOnBid(this::onBidPressed);
            itemBox.getChildren().add(iv);
        }
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

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                String msg = String.format(
                        "You have been outbid by %s on %s",
                        bidder.getName(), item.getName()
                );
                alert.setContentText(msg);
                alert.show();
            });
        } else if (item.getWinnerId() == this.accountId) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                String msg = String.format(
                        "You have won the auction on %s",
                        item.getName()
                );
                alert.setContentText(msg);
                alert.show();
            });
        }
    }

    private void handleAccountChanged() {
        this.bankClient.sendRequest(new Request("accounts.get",
                "id", Integer.toString(this.accountId)));
        Response r = this.bankClient.waitForResponse();

        int funds = ((Account) r.getData()).getFunds();
        this.fundsLabel.setText(Integer.toString(funds));
    }

    private void onBidPressed(int itemId) {
        this.auctionClient.sendRequest(new Request("items.get",
                "id", Integer.toString(itemId)));
        Response r = this.auctionClient.waitForResponse();
        Item item = (Item) r.getData();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/gui/fxml/bid.fxml"
        ));

        Parent p = null;
        try {
            p = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        BidController controller = loader.getController();
        controller.setAccountId(this.accountId);
        controller.setAuctionClient(this.auctionClient);
        controller.setItem(item, itemId);

        Stage stage = new Stage();
        stage.setScene(new Scene(p));
        stage.show();
    }

    public void setAccountId(int id) {
        this.accountId = id;
    }
}
