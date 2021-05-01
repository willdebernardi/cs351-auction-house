package server.store;

import server.Event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class Resource<T> {
    private ArrayList<T> resources;
    private ArrayList<Set<Listener>> listeners;
    private Supplier<T> creator;
    private String name;

    /**
     * Creates a new Resource
     * @param creator function used in the create method to create a new object
     *                of type T
     */
    public Resource(String name, Supplier<T> creator) {
        this.resources = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.name = name;
    }

    /**
     * Creates a new Resource and returns its ID number.
     *
     * @return the ID of the newly created Resource.
     */
    public int create() {
       this.resources.add(creator.get());
       this.listeners.add(new HashSet<>());
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
    public void putResource(int id, T r) {
        this.resources.set(id, r);

        // notify listeners of the change
        for (Listener l : this.listeners.get(id)) {
            l.notify(new Event(id, name));
        }
    }

    /**
     * Removes the resource with the given id.
     *
     * @param id id of the resource
     */
    public void removeResource(int id) {
        this.resources.remove(id);
    }

    /**
     * Registers a listener to the resource with the given id.
     *
     * @param id id of the resource
     * @param listener the listener
     */
    public void registerListener(int id, Listener listener) {
        this.listeners.get(id).add(listener);
    }

    /**
     * @return the name of the resource (for example, 'accounts')
     */
    public String getName() {
        return name;
    }
}
