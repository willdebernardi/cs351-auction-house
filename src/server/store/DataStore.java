/**
 * A collection of Resource objects as a singleton.
 *
 * @author Christopher Medlin, Will Debernardi, Isaiah Martell
 */
package server.store;

import java.util.Set;

public class DataStore {
    private static DataStore instance = null;

    private Set<Resource> resourceSet;

    /**
     * Return the DataStore singleton instance, creating one if it is null.
     * @return the DataStore
     */
    public static DataStore getInstance() {
        if (instance == null ) {
            instance = new DataStore();
        }
        return instance;
    }

    /**
     * Creates the DataStore instance if it has not been created yet.
     *
     * @param resources the resources to initialize it with.
     * @throws IllegalStateException if the DataStore already exists.
     */
    public static void instantiate(Resource... resources)
    throws IllegalStateException {
        if (instance == null) {
            instance = new DataStore(resources);
        } else {
            throw new IllegalStateException("Data store already created.");
        }
    }

    private DataStore(Resource... resources) {
        for (Resource r : resources) {
            addResource(r);
        }
    }

    /**
     * Adds a new resource (such as accounts) to the DataStore
     *
     * @param r the resource
     */
    public void addResource(Resource r) {
        this.resourceSet.add(r);
    }

    /**
     * Registers a listener to a particular resource specified by the url.
     *
     * @param url a url of the form (resourcename).(resourceid). for example,
     *            accounts.439
     * @param listener a listener to send an Event object to when the specified
     *                 resource has been changed
     * @throws IllegalArgumentException if an invalid url is given or a
     *                                  non-existent resource is requested
     */
    public void registerListener(String url, Listener listener)
    throws IllegalArgumentException {
        String[] split = url.split(".");
        int id = -1;
        if (split.length != 2) {
            throw new IllegalArgumentException(
                    "Invalid url."
            );
        }
        try {
            id = Integer.parseInt(split[2]);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid resource ID."
            );
        }
        Resource r = getResource(split[0]);
        if (r == null) {
            throw new IllegalArgumentException("Resource does not exist.");
        }
        r.registerListener(id, listener);
    }

    /**
     * Returns a resource s.t. resource.getName() == name and the resource is
     * in the DataStore
     *
     * @param name the name of the resource
     * @return the resource with the given name
     */
    public Resource getResource(String name) {
        for (Resource r : resourceSet) {
            if (r.getName() == name) {
                return r;
            }
        }
        return null;
    }
}
