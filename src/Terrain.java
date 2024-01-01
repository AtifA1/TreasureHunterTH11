import java.awt.Color;

/**
 * The Terrain class is designed to represent the zones between the towns in the Treasure Hunter game.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */
public class Terrain {
    // Instance variables
    private String terrainName;
    private String neededItem;
    private OutputWindow outputWindow;

    /**
     * Sets the class member variables.
     *
     * @param name The name of the zone.
     * @param item The item needed in order to cross the zone.
     */
    public Terrain(String name, String item, OutputWindow outputWindow) {
        terrainName = name;
        neededItem = item.toLowerCase();
        this.outputWindow = outputWindow;
    }

    /**
     * Accessor method to get the name of the terrain.
     *
     * @return The name of the terrain.
     */
    public String getTerrainName() {
        return terrainName;
    }

    /**
     * Accessor method to get the needed item to cross the terrain.
     *
     * @return The needed item.
     */
    public String getNeededItem() {
        return neededItem;
    }

    /**
     * Guards against a hunter crossing the zone without the proper item.
     * Searches the hunter's inventory for the proper item and determines whether the hunter can cross.
     *
     * @param hunter The Hunter object trying to cross the terrain.
     * @return True if the Hunter has the proper item.
     */
    public boolean canCrossTerrain(Hunter hunter) {
        if (hunter.hasItemInKit(neededItem)) {
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of the terrain and item to cross it.
     *
     * @return A string representation of the terrain and needed item.
     */
    public String toString() {
        return terrainName + " needs a(n) " + neededItem + " to cross.";
    }
}
