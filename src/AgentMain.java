import gui.controllers.DropdownController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.Client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class AgentMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        List<String> argsList = getParameters().getRaw();
        if (!argsList.isEmpty()) {
            String hostName = argsList.get(0);
            InetAddress ipAddress = InetAddress.getByName(hostName);
            int port = Integer.parseInt(argsList.get(1));

            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/gui/fxml/dropDown.fxml"));
            Parent root = loader.load();
            DropdownController controller = loader.getController();

            Client client = new Client(ipAddress, port);
            client.connect();
            controller.initializeComboBox(client);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Auction House Selection");
            primaryStage.show();
        }
        else {
            throw new IllegalArgumentException();
        }
    }
}
