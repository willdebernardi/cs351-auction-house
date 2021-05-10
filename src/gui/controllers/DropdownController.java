/**
 * Controller for the auction selection window.
 *
 * @author Will Debernardi, Isaiah Martell, Christopher Medlin
 */
package gui.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import resource.Auction;
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
    private Client client;

    public void initializeComboBox(Client client) {
        client.sendRequest(new Request("auctions.list"));
        Response response = client.waitForResponse();
        Set<Auction> auctionSet = (Set<Auction>) response.getData();
        for (Auction auction : auctionSet) {
            auctionHouseComboBox.getItems().add(auction);
        }
        this.client = client;
    }

    public void dropdownEvent(Event e) {
        try {
            Auction auctionSelected = auctionHouseComboBox.getValue();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/resources/fxml/main.fxml")
            );
            Parent p = loader.load();

            MainController controller = loader.getController();
            controller.connectToAuction(
                    auctionSelected.getIp(),
                    auctionSelected.getPort()
            );

            // create bank account;
            client.sendRequest(new Request("accounts.create",
                    "name", "agent", "funds", "10000"));
            int id = (int) client.waitForResponse().getData();
            controller.setAccountId(id);
            controller.setAuctionAccountId(auctionSelected.getAccountId());

            controller.connectToBank(client);
            Stage stage = new Stage();
            stage.setScene(new Scene(p));
            stage.show();

            controller.refresh();
            mainPane.getScene().getWindow().hide();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
