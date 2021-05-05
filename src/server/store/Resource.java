package server.store;

import server.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class Resource<T> {
    private HashMap<Integer, T> resources;
    private HashMap<Integer, Set<Listener>> listeners;
    private Supplier<T> creator;
    private String name;
    private static int highestId;

    /**
     * Creates a new Resource
     * @param creator function used in the create method to create a new object
     *                of type T
     */
    public Resource(String name, Supplier<T> creator) {
        this.resources = new HashMap<>();
        this.listeners = new HashMap<>();
        this.creator = creator;
        this.name = name;
    }

    /**
     * Creates a new Resource and returns its ID number.
     *
     * @return the ID of the newly created Resource.
     */
    public synchronized int create() {
       this.resources.put(++highestId, creator.get());
       this.listeners.put(highestId, new HashSet<>());
       return resources.size()-1;
    }

    /**
     * Gets the resource with the given ID.
     *
     * @param id the id of the resource to be retrieved
     * @return the resource
     */
    public T getResource(int id) {
        return this.resources.get(id);
    }

    /**
     * Changes the resource with the given id.
     *
     * @param id id of the resource
     * @param r updated resource
     */
    public synchronized void putResource(int id, T r) {
        this.resources.put(id, r);

        // notify listeners of the change
        // if the listener fails to be notified (if l.notify returns false),
        // remove it from the set of listeners
        this.listeners.get(id).removeIf(l -> !l.notify(new Event(id, name)));
    }

    /**
     * Removes the resource with the given id.
     *
     * @param id id of the resource
     */
    public synchronized void removeResource(int id) {
        this.resources.remove(id);
        this.listeners.remove(id);
    }

    /**
     * Registers a listener to the resource with the given id.
     *
     * @param id id of the resource
     * @param listener the listener
     */
    public synchronized void registerListener(int id, Listener listener) {
        this.listeners.get(id).add(listener);
    }

    /**
     * @return the name of the resource (for example, 'accounts')
     */
    public String getName() {
        return name;
    }

    /**
     * @return all of the particulars of this resource.
     */
    public Set<T> list() {
        return new HashSet<>(this.resources.values());
    }
}
