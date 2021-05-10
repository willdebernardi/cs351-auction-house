/**
 * Main controller for the GUI.
 *
 * @author Will Debernardi, Isaiah Martell, Christopher Medlin
 */
package gui.controllers;

import gui.ItemView;
import javafx.application.Platform;
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
    public void connectToBank(Client c) {
        this.bankClient = c;
        this.bankClient.setOnEvent(this::handleEvent);
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
        updateFundsLabel();
    }

    // called when the client receives an event
    private void handleEvent(Event e) {
        if (e.getResourceName().equals("items")) {
            this.handleItemChanged(e.getChangedID());
        } else if (e.getResourceName().equals("accounts")) {
            // no need to pass ID, since the only account we're listening to is
            // our own.
            this.updateFundsLabel();
        }
        Platform.runLater(this::refresh);
    }

    private void handleItemChanged(int id) {
        this.auctionClient.sendRequest(new Request("items.get",
                "id", Integer.toString(id)));
        Response r = this.auctionClient.waitForResponse();
        Item item = (Item) r.getData();

        // if a winner that isn't this agent has been picked
        if (item.getWinnerId() != -1 && item.getWinnerId() != accountId) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                String msg = String.format(
                        "You have lost the auction on %s",
                        item.getName()
                );
                alert.setContentText(msg);
                alert.show();
            });
        }

        // if the highest bidder is not this agent
        else if (item.getBidderId() != this.accountId) {
            Platform.runLater(() -> {
                this.auctionClient.sendRequest(new Request("accounts.get",
                        "id", Integer.toString(item.getBidderId())));
                Response response = this.bankClient.waitForResponse();
                Account bidder = (Account) response.getData();
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
                payToAuction(item.getHighestBid());
            });
        }
    }

    private void payToAuction(int funds) {
        Response r = null;
        this.bankClient.sendRequest(new Request("accounts.transfer",
                "id1", Integer.toString(this.accountId),
                "id2", Integer.toString(this.auctionAccountId),
                "funds", Integer.toString(funds)
        ));
        r = bankClient.waitForResponse();
        if (r.getType() != Response.Type.OK) {
            System.out.println("Error transferring funds to auction account.");
        }
    }

    private void updateFundsLabel() {
        this.bankClient.sendRequest(new Request("accounts.get",
                "id", Integer.toString(this.accountId)));
        Response r = this.bankClient.waitForResponse();

        Account a = (Account) r.getData();
        int funds = a.getFunds();
        Platform.runLater(() -> {
            this.fundsLabel.setText("Funds: " + Integer.toString(funds));
        });
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

    public void setAuctionAccountId(int id) {
        this.auctionAccountId = id;
    }
}
