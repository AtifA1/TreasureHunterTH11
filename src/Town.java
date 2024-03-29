/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private boolean toughTown;
    private String treasure;
    private boolean searchedTown;
    private OutputWindow outputWindow;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, OutputWindow outputWindow) {
        this.shop = shop;
        this.terrain = getNewTerrain();
        this.treasure = getTreasure();

        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;

        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
        this.outputWindow = outputWindow;


    }

    public String getLatestNews() {
        return printMessage;
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";

        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown(boolean easyMode) {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak() && !easyMode) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + item + ".";
            }

            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) {
        shop.enter(hunter, choice);
        printMessage = "You left the shop." + "\n";
    }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }


        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage = "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n";
            int goldDiff = (int) (Math.random() * 10) + 1;
            if (hunter.hasItemInKit("sword")) {
                printMessage += "the brawler, seeing your sword, realizes he picked a losing fight and gives you his gold";
                hunter.changeGold(goldDiff);
            } else if (Math.random() > noTroubleChance) {
                printMessage +=  "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " + goldDiff + " gold.";
                hunter.changeGold(goldDiff);
            } else {
                printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
                printMessage += "\nYou lost the brawl and pay " + goldDiff + " gold.";
                hunter.changeGold(-goldDiff);
            }
        }
    }

    public String toString() {
        return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random();
        if (rnd < (double) 1 / 6) {
            return new Terrain("Mountains", "Rope", outputWindow);
        } else if (rnd < (double) 2 / 6) {
            return new Terrain("Ocean", "Boat", outputWindow);
        } else if (rnd < (double) 3 / 6) {
            return new Terrain("Plains", "Horse", outputWindow);
        } else if (rnd < (double) 4 / 6) {
            return new Terrain("Desert", "Water", outputWindow);
        } else if (rnd < (double) 5 / 6) {
            return new Terrain("Jungle", "Machete", outputWindow);
        } else {
            return new Terrain("Marsh", "Boots", outputWindow);
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }

    public boolean digForGold(boolean dugOnce) {
        if (!dugOnce) {
            if (hunter.hasItemInKit("shovel")) {
                int fiftyPercent = (int) (Math.random() * 2) + 1;
                if (fiftyPercent == 1) {
                    int gold = (int) (Math.random() * 20) + 1;
                    printMessage = "You dug up " + gold + " gold!";
                    hunter.changeGold(gold);
                    return true;
                } else {
                    printMessage = "You dug but only found dirt!";
                    return true;
                }
            } else {
                printMessage = "You can't dig for gold without a shovel!";
                return false;
            }
        } else {
            printMessage = "You already dug for gold in this town.";
            return false;
        }
    }

    public String getTreasure() {
        double rnd = Math.random();
        if (rnd < .33) {
            return "crown";
        } else if (rnd < .66) {
            return "trophy";
        } else if (rnd < .99) {
            return "gem";
        } else {
            return "dust";
        }
    }

    public void huntForTreasure() {
        if (searchedTown) {
            printMessage = "You have already searched this town.";
        } else {
            if (treasure.equals("dust")) {
                printMessage = "You found dust";
            } else {
                if (hunter.hasTreasure(treasure)) {
                    printMessage = "You already collected " + treasure;
                } else {
                    printMessage = "You found a " + treasure + "!";
                    hunter.addTreasure(treasure);
                }
                searchedTown = true;
            }
        }
    }
}