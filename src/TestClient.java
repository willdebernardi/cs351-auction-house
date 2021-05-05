import server.Client;
import server.Endpoint;
import server.Request;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;

public class TestClient {

    public static void main(String[] args) {
        Client client = null;
        if (args.length == 2) {
            try {
                InetAddress address = InetAddress.getByName(args[0]);
                int port = Integer.parseInt(args[1]);
                client = new Client(address,port);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        client.connect();
        client.setOnEvent(System.out::println);
        client.setOnResponse(System.out::println);
        Scanner scanner = new Scanner(System.in);
        String inputURL;
        System.out.println("Type endpoint URL");
        inputURL = scanner.nextLine();
        while (!inputURL.equals("STOP")) {
            System.out.println("Input key");
            String key = scanner.nextLine();

            HashMap<String, String> parameters = new HashMap<>();

            while (!key.equals("STOP")) {
                System.out.println("Input value");
                parameters.put(key, scanner.nextLine());
                System.out.println("Input key");
                key = scanner.nextLine();
            }
            Request request = new Request(inputURL, parameters);
            client.sendRequest(request);
            System.out.println("Type endpoint URL");
            inputURL = scanner.nextLine();
        }
    }
}
