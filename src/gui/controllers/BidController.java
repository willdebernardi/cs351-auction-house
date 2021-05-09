/**
 * Controller for the bid window.
 *
 * @author Will Debernardi, Isaiah Martell, Christopher Medlin
 */
package gui.controllers;

import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import resources.Item;
import server.Client;
import server.Request;
import server.Response;

public class BidController {
    @FXML
    Label itemName;
    @FXML
    Label highestBid;
    @FXML
    Client auctionClient;
    @FXML
    TextField bidField;

    private int itemId;
    private int accountId;

    public void setItem(Item item, int itemId) {
        this.itemName.setText(item.getName());
        this.highestBid.setText(
                "Highest bid: " + Integer.toString(item.getHighestBid())
        );
        this.itemId = itemId;
        itemName.setFont(new Font(20));
    }

    public void confirm() {
        auctionClient.sendRequest(new Request("items.bid",
            "id", Integer.toString(itemId),
            "funds", bidField.getText(),
            "accountId", Integer.toString(this.accountId)
        ));

        Response r = auctionClient.waitForResponse();

        if (r.getType() == Response.Type.ERROR) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(r.getMessage());
            alert.showAndWait();
            return;
        }

        auctionClient.sendRequest(new Request(
                "listen",
                "url", "items." + Integer.toString(itemId)
        ));

        bidField.getScene().getWindow().hide();
    }

    public void setAuctionClient(Client c) {
        this.auctionClient = c;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
