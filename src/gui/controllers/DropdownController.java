package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import resources.Auction;
import server.Client;
import server.Request;
import server.Response;

import java.util.Set;

public class DropdownController  {

    @FXML
    private Pane mainPane;
    @FXML
    private ComboBox<Auction> auctionHouseComboBox;
    @FXML
    private Button submitButton;

    public void initializeComboBox(Client client) {
        client.sendRequest(new Request("auctions.list"));
        Response response = client.waitForResponse();
        Set<Auction> auctionSet = (Set<Auction>) response.getData();
        for (Auction auction : auctionSet) {
            auctionHouseComboBox.getItems().add(auction);
        }
        dropdownEvent();
    }

    public void dropdownEvent() {
        try {
            Auction auctionSelected = auctionHouseComboBox.getValue();
            System.out.println(auctionSelected.toString());
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
