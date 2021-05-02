/**
 * An event sent when a resource that the client is listening to is changed.
 *
 * @author Will Debernardi, Isaiah Martell, Christopher Medlin
 */
package server;

public class Event {
    private int changedID;
    private String resourceName;

    /**
     * @param changedID the ID of the resource that has been changed
     * @param resourceName the universal identifier for the resource
     */
    public Event(int changedID, String resourceName) {
        this.changedID = changedID;
        this.resourceName = resourceName;
    }

    /**
     * @return the id of the changed resource
     */
    public int getChangedID() {
        return changedID;
    }

    /**
     * @return the name of the universal resource that the changed one belongs
     *         to (for instance, 'accounts')
     */
    public String getResourceName() {
        return resourceName;
    }
}
