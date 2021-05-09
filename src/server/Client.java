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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class Client {

    private InetAddress ipAddress;
    private int port;
    private Socket clientSocket;
    private ObjectOutputStream os;
    private ObjectInputStream io;
    private Consumer<Event> onEvent;
    private Consumer<Response> onResponse;

    private CountDownLatch receivedSignal;
    // is set to false when a request is sent, and then set to true when a
    // response is received
    private AtomicBoolean received;
    private Response response;
    private ExecutorService threadPool;

    public Client(InetAddress ipAddress, int port) {
        this.onEvent = (r) -> {};
        this.onResponse = (r) -> {};
        this.ipAddress = ipAddress;
        this.port = port;
        this.receivedSignal = new CountDownLatch(1);
        this.received = new AtomicBoolean(false);
        this.threadPool = Executors.newCachedThreadPool();
    }

    public InetAddress getHost() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void connect() {
        try {
            this.clientSocket = new Socket(this.ipAddress, this.port);
            this.os = new ObjectOutputStream(
                    this.clientSocket.getOutputStream()
            );
            this.io = new ObjectInputStream(
                    this.clientSocket.getInputStream()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        //listenToInputStream is now runnable
        threadPool.execute(this::listenToInputStream);
    }

    public void stop() {
        try {
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.threadPool.shutdown();
    }

    public void sendRequest(Request r) {
        received.set(false);
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

    /**
     * Waits until a response has been received, then passes to onResponse.
     */
    public Response waitForResponse() {
        if (this.received.get()) {
            received.set(false);
            return this.response;
        }
        // wait for the countdown latch to signal
        try {
            receivedSignal.await();
            return this.response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void listenToInputStream() {
        while (!clientSocket.isClosed()) {
            Object obj = null;
            try {
                obj = io.readObject();
            } catch (Exception e) {
                break;
            }

            if (obj instanceof Event) {
                onEvent.accept((Event) obj);
            } else if (obj instanceof Response) {
                this.response = (Response) obj;
                received.set(true);
                // signal to main thread that a response has been received
                // incase waitForResponse is being used.
                receivedSignal.countDown();
                receivedSignal = new CountDownLatch(1);
                onResponse.accept((Response) obj);
            } else {
                System.out.println("Object not response or event");
            }
        }
    }
}
