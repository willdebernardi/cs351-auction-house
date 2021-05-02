package server.store;


import server.Event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Listener {
    private ObjectOutputStream out;

    /**
     * Creates a new listener for the given socket.
     *
     * @param s the socket to send Events to.
     */
    public Listener(Socket s) {
        try {
            this.out = new ObjectOutputStream(s.getOutputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException("Socket already closed.");
        }
    }

    /**
     * Sends an event to the listener.
     *
     * @param e the event
     * @return true if the listener received the event, false if an error
     *         occurred.
     */
    public boolean notify(Event e) {
        try {
            this.out.writeObject(e);
        } catch (IOException exc) {
            return false;
        }
        return true;
    }
}
