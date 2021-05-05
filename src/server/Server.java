package server;

import server.store.DataStore;
import server.store.Listener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Isaiah Martell, Christopher Medlin, Will Debernardi
 * @date 26 Apr 2021
 */
public class Server {

    private HashMap<String, Endpoint> endpoints;
    private ServerSocket serverSocket;

    /**
     * Creates a new server. By default registers an endpoint with the url
     * "listen", which registers a client as a listener for some Resource in the
     * DataStore.
     *
     * @param port the port for the Server to listen on
     */
    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            endpoints = new HashMap<>();
        }
        catch (IOException ex) {
            System.out.println("Error creating server");
            ex.printStackTrace();
        }
    }

    /**
     * Starts the server, listening on the port specified in the constructor.
     * Creates new threads for each new connection.
     *
     */
    public void run() {
        //Create open ended thread pool
        ExecutorService threadPool = Executors.newCachedThreadPool();
        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Waiting for a connection to server");
                //Wait for client connection (blocking call)
                Socket clientSocket = serverSocket.accept();
                /* Create new client handler object for
                   socket and fork it in a background thread */
                threadPool.submit(new ClientHandler(clientSocket));
            }
        }
        catch(IOException ex) {
            System.out.println("Error starting server");
            ex.printStackTrace();
        }
        finally {
            threadPool.shutdown();
        }
    }

    /**
     * If server is running, stop it by killing the socket
     *
     */
    public void stop() {
        try {
            serverSocket.close();
        }
        catch (IOException e) {
            System.out.println("Error shutting down server");
            e.printStackTrace();
        }
    }

    /**
     * Registers a new endpoint for the server, to be specified by the endpoint
     * URL in a Request object when a Client makes a request.
     *
     * @param endpoint the endpoint
     */
    public void addEndpoint(Endpoint endpoint) {
        endpoints.put(endpoint.getUrl(), endpoint);
    }

    private class ClientHandler implements Runnable {

        private Socket clientConnection;
        private ObjectInputStream io;
        private ObjectOutputStream os;

        public ClientHandler(Socket clientSocket) {
            try {
                this.clientConnection = clientSocket;
                this.os = new ObjectOutputStream(
                        this.clientConnection.getOutputStream()
                );
                this.io = new ObjectInputStream(
                        this.clientConnection.getInputStream()
                );
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (!clientConnection.isClosed()) {
                try {
                    Object object = io.readObject();
                    if (!(object instanceof Request)) {
                        errorMessage("Object not request");
                    }
                    Request r = (Request) object;

                    if (r.getEndpointUrl() == "listen") {
                        listen(r);
                        continue;
                    }

                    if (!endpoints.containsKey(r.getEndpointUrl())) {
                        errorMessage("Invalid endpoint");
                    }
                    Endpoint endpoint = endpoints.get(r.getEndpointUrl());
                    os.writeObject(endpoint.call(r));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        private void errorMessage(String msg) throws IOException {
            os.writeObject(new Response(msg, null, Response.Type.ERROR));
        }

        private void listen(Request r) {
            Listener l = new Listener(clientConnection);
            try {
                DataStore ds = DataStore.getInstance();
                ds.registerListener(r.getParameter("url"), l);
            } catch (IllegalArgumentException e) {
                try {
                    errorMessage(e.getMessage());
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        }
    }
}
