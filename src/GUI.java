import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import server.Client;
import server.Request;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class GUI extends Application {

    @FXML
    private Pane mainPane;
    @FXML
    private ComboBox<Object> auctionHouseComboBox;
    @FXML
    private Button submitButton;

    private String hostName;
    private int port;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        List<String> argsList = getParameters().getRaw();
        hostName = argsList.get(0);
        port = Integer.parseInt(argsList.get(1));
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("resources/view.fxml"));
        Parent root = loader.load();

        InetAddress ipAddress = InetAddress.getByName(hostName);
        Client client = new Client(ipAddress, port);
        client.sendRequest(new Request("auctions.list"));
        dropdownEvent();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Auction House Selection");
        primaryStage.show();
    }

    public void dropdownEvent() {
        submitButton.setOnAction(event -> {
            try {

            }
            catch (Exception ex) {
                System.out.println(ex);
            }
        });
    }
}
