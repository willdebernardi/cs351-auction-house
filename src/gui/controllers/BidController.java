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

    int itemId;
    int accountId;

    public void setItem(Item item, int itemId) {
        this.itemName.setText(item.getName());
        this.highestBid.setText(Integer.toString(item.getBidderId()));
        this.itemId = itemId;
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
        }

        bidField.getScene().getWindow().hide();
    }

    public void setAuctionClient(Client c) {
        this.auctionClient = c;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
