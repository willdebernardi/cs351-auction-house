package gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import resources.Item;
import server.Client;
import server.Request;
import server.Response;

import java.util.function.Consumer;

public class ItemView extends HBox {
    private final Label nameLabel;
    private final Button bidButton;
    private final Client client;
    private final int itemId;
    private Consumer<Integer> onBid;

    /**
     * Retrieves the item specified by itemId and constructs a gui.controllers.GUI view around
     * it
     *
     * @param itemId the item id
     * @param client the client to use to retrieve the item from the auction
     *               server
     */
    public ItemView(int itemId, int accountId, Client client) {
        client.sendRequest(new Request("items.get",
                "id", Integer.toString(itemId)));
        Response response = client.waitForResponse();
        Item item = (Item) response.getData();

        this.nameLabel = new Label(item.getName());
        this.bidButton = new Button("Bid");
        this.client = client;
        this.itemId = itemId;
        this.onBid = (i) -> {};
        this.setSpacing(50);

        this.getChildren().add(nameLabel);
        this.getChildren().add(bidButton);
        this.bidButton.setOnAction((e) -> onBid.accept(this.itemId));
    }

    /**
     * Sets the function to be called when the bid button is pressed.
     *
     * @param onBid a consumer that is called with the item ID as a parameter
     *              when the bid button is pressed
     */
    public void setOnBid(Consumer<Integer> onBid) {
        this.onBid = onBid;
    }
}
