import java.awt.Color;

/**
 * Hunter Class
 * This class represents the treasure hunter character (the player) in the Treasure Hunt game.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */
public class Hunter {
    // Instance variables
    private String hunterName;
    private String[] kit;
    private int gold;

    private String[] treasure;
    private static boolean samurai;
    private OutputWindow outputWindow;

    /**
     * The base constructor of a Hunter assigns the name to the hunter and an empty kit.
     *
     * @param hunterName   The hunter's name.
     * @param startingGold The gold the hunter starts with.
     * @param samurai      Whether the hunter is a samurai or not.
     */
    public Hunter(String hunterName, int startingGold, boolean samurai, OutputWindow outputWindow) {
        this.hunterName = hunterName;
        kit = new String[7];
        if (samurai) {
            kit = new String[8];
        }
        treasure = new String[3];
        gold = startingGold;
        this.samurai = samurai;
        this.outputWindow = outputWindow;
    }

    /**
     * Accessor method to get the hunter's name.
     *
     * @return The hunter's name.
     */
    public String getHunterName() {
        return hunterName;
    }

    /**
     * Accessor method to check if the hunter is a samurai.
     *
     * @return True if the hunter is a samurai, false otherwise.
     */
    public static boolean getSamurai() {
        return samurai;
    }

    /**
     * Modifies the amount of gold the hunter has.
     *
     * @param modifier Amount to modify gold by.
     */
    public void changeGold(int modifier) {
        gold += modifier;
        if (gold < 0) {
            outputWindow.addTextToWindow("You ran out of gold! Game over!", Color.red);
            System.exit(0);
        }
    }

    /**
     * Allows the hunter to buy an item from a shop.
     *
     * @param item       The item the hunter is buying.
     * @param costOfItem The cost of the item.
     * @return True if the item is successfully bought.
     */
    public boolean buyItem(String item, int costOfItem) {
        if ((costOfItem == 0 && !Hunter.getSamurai()) || gold < costOfItem || hasItemInKit(item)) {
            return false;
        }

        gold -= costOfItem;
        addItem(item);
        return true;
    }

    /**
     * Allows the hunter to sell an item to a shop for gold.
     *
     * @param item         The item being sold.
     * @param buyBackPrice The amount of gold earned from selling the item.
     * @return True if the item was successfully sold.
     */
    public boolean sellItem(String item, int buyBackPrice) {
        if (buyBackPrice <= 0 || !hasItemInKit(item)) {
            return false;
        }

        gold += buyBackPrice;
        removeItemFromKit(item);
        return true;
    }

    /**
     * Removes an item from the kit by setting the index of the item to null.
     *
     * @param item The item to be removed.
     */
    public void removeItemFromKit(String item) {
        int itmIdx = findItemInKit(item);

        // If item is found
        if (itmIdx >= 0) {
            kit[itmIdx] = null;
        }
    }

    /**
     * Checks to make sure that the item is not already in the kit.
     * If not, it assigns the item to an index in the kit with a null value ("empty" position).
     *
     * @param item The item to be added to the kit.
     * @return True if the item is not in the kit and has been added.
     */
    public boolean addItem(String item) {
        if (!hasItemInKit(item)) {
            int idx = emptyPositionInKit();
            kit[idx] = item;
            return true;
        }

        return false;
    }

    /**
     * Adds a treasure to the hunter's collection.
     *
     * @param item The treasure item to be added.
     */
    public void addTreasure(String item) {
        int idx = emptyPositionInTreasure();
        treasure[idx] = item;
    }

    /**
     * Checks if the kit array has the specified item.
     *
     * @param item The search item.
     * @return True if the item is found.
     */
    public boolean hasItemInKit(String item) {
        for (String tmpItem : kit) {
            if (item.equals(tmpItem)) {
                // Early return
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the hunter has a specific treasure.
     *
     * @param item The treasure item to check.
     * @return True if the hunter has the treasure.
     */
    public boolean hasTreasure(String item) {
        for (String tmpItem : treasure) {
            if (item.equals(tmpItem)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns a printable representation of the inventory, which
     * is a list of the items in kit, with a space between each item.
     *
     * @return The printable String representation of the inventory.
     */
    public String getInventory() {
        String printableKit = "";
        String space = " ";

        for (String item : kit) {
            if (item != null) {
                printableKit += item + space;
            }
        }
        return printableKit;
    }

    /**
     * Returns a printable representation of the list of treasures found.
     *
     * @return The printable String representation of the treasure list.
     */
    public String getTreasureList() {
        String printableList = "";
        if (treasureListIsEmpty()) {
            printableList += "none";
        }
        for (String item : treasure) {
            if (item != null) {
                printableList += "a " + item + " ";
            }
        }
        return printableList;
    }

    /**
     * Returns a string representation of the hunter, including their name, gold, inventory, and treasures found.
     *
     * @return A string representation of the hunter.
     */
    public String toString() {
        String str = hunterName + " has " + gold + " gold";
        if (!kitIsEmpty()) {
            str += " and " + getInventory();
        }
        if (!treasureListIsEmpty()) {
            str += "\nTreasures found: " + getTreasureList();
        }
        return str;
    }

    /**
     * Searches kit array for the index of the specified value.
     *
     * @param item String to look for.
     * @return The index of the item, or -1 if not found.
     */
    private int findItemInKit(String item) {
        for (int i = 0; i < kit.length; i++) {
            String tmpItem = kit[i];

            if (item.equals(tmpItem)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Checks if the kit is empty - meaning all elements are null.
     *
     * @return True if kit is completely empty.
     */
    private boolean kitIsEmpty() {
        for (String string : kit) {
            if (string != null) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the list of treasures is empty.
     *
     * @return True if the list of treasures is empty.
     */
    private boolean treasureListIsEmpty() {
        for (String string : treasure) {
            if (string != null) {
                return false;
            }
        }

        return true;
    }

    /**
     * Finds the first index where there is a null value in the kit array.
     *
     * @return The index of an empty slot in the kit, or -1 if not found.
     */
    private int emptyPositionInKit() {
        for (int i = 0; i < kit.length; i++) {
            if (kit[i] == null) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Finds the first index where there is a null value in the treasure array.
     *
     * @return The index of an empty slot in the treasure list, or -1 if not found.
     */
    private int emptyPositionInTreasure() {
        for (int i = 0; i < treasure.length; i++) {
            if (treasure[i] == null) {
                return i;
            }
        }

        return -1;
    }
}
