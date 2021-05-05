/**
 * Handles client communication.
 *
 * @author Isaiah Martell, Christopher Medlin, Will Debernardi
 * @date 4 May 2021
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Client {

    private InetAddress ipAddress;
    private int port;
    private Socket clientSocket;
    private ObjectOutputStream os;
    private ObjectInputStream io;
    private Consumer<Event> onEvent;
    private Consumer<Response> onResponse;

    public Client(InetAddress ipAddress, int port) {
        try {
            this.onEvent = (r) -> {};
            this.onResponse = (r) -> {};
            this.ipAddress = ipAddress;
            this.port = port;
            this.os = new ObjectOutputStream(
                    this.clientSocket.getOutputStream()
            );
            this.io = new ObjectInputStream(
                    this.clientSocket.getInputStream()
            );
        }
        catch (IOException ex) {
            System.out.println("Error creating client");
            ex.printStackTrace();
        }
    }

    public void connect() {
        try {
            this.clientSocket = new Socket(this.ipAddress, this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ExecutorService threadPool = Executors.newCachedThreadPool();
        //listenToInputStream is now runnable
        threadPool.execute(this::listenToInputStream);
    }

    public void sendRequest(Request r) {
        try {
            os.writeObject(r);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOnEvent(Consumer<Event> onEvent) {
        this.onEvent = onEvent;
    }

    public void setOnResponse(Consumer<Response> onResponse) {
        this.onResponse = onResponse;
    }

    public void listenToInputStream() {
        while (!clientSocket.isClosed()) {
            try {
                Object obj = io.readObject();
                if (obj instanceof Event) {
                    onEvent.accept((Event) obj);
                }
                else if (obj instanceof Response) {
                    onResponse.accept((Response) obj);
                }
                else {
                    System.out.println("Object not response or event");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
