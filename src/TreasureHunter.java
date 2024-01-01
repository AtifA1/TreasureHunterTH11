import java.awt.Color;
import java.util.Scanner;

public class TreasureHunter {
    private static final Scanner SCANNER = new Scanner(System.in);
    private final OutputWindow outputWindow;

    private Town currentTown;
    private Hunter hunter;
    private boolean hardMode;
    private boolean easyMode;
    private boolean dugOnce;
    private boolean samuraiMode;

    public TreasureHunter(OutputWindow outputWindow) {
        this.outputWindow = outputWindow;
        currentTown = null;
        hunter = null;
        hardMode = false;
        dugOnce = false;
    }

    public void play() {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    private void welcomePlayer() {
        outputWindow.addTextToWindow("Welcome to TREASURE HUNTER!\n", Color.blue);
        outputWindow.addTextToWindow("Going hunting for the big treasure, eh?\n", Color.blue);
        outputWindow.addTextToWindow("What's your name, Hunter? ", Color.black);
        String name = SCANNER.nextLine().toLowerCase();
        hunter = new Hunter(name, 10, false, outputWindow);

        outputWindow.addTextToWindow("\nDifficulty (E)asy/(N)ormal/(H)ard: ", Color.black);
        String mode = SCANNER.nextLine().toLowerCase();
        if (mode.equals("h")) {
            hardMode = true;
        } else if (mode.equals("test")) {
            // Test mode initialization
            hunter = new Hunter(name, 172, false, outputWindow);
            initializeTestInventory();
        } else if (mode.equals("e")) {
            easyMode = true;
            hunter = new Hunter(name, 20, false, outputWindow);
        } else if (mode.equals("s")) {
            samuraiMode = true;
            hunter = new Hunter(name, 20, true, outputWindow);
        }
    }

    private void initializeTestInventory() {
        // Initialize test inventory
        hunter.buyItem("water", 2);
        hunter.buyItem("rope", 4);
        hunter.buyItem("machete", 6);
        hunter.buyItem("horse", 12);
        hunter.buyItem("boat", 20);
        hunter.buyItem("boots", 20);
        hunter.buyItem("shovel", 8);
    }

    private void enterTown() {
        double markdown = 0.5;
        double toughness = 0.4;
        if (hardMode) {
            markdown = 0.25;
            toughness = 0.75;
        } else if (easyMode) {
            markdown = 1;
            toughness = 0.99;
        }

        Shop shop = new Shop(markdown, outputWindow);
        currentTown = new Town(shop, toughness, outputWindow);
        currentTown.hunterArrives(hunter);
    }

    private void showMenu() {
        String choice = "";

        while (!choice.equals("x")) {
            outputWindow.clear();

            if (hunter.hasTreasure("crown") && hunter.hasTreasure("trophy") && hunter.hasTreasure("gem")) {
                outputWindow.addTextToWindow("Congratulations, you have found the last of the three treasures, you win!\n", Color.green);
                break;
            }

            outputWindow.addTextToWindow(currentTown.getLatestNews() + "\n", Color.blue);
            outputWindow.addTextToWindow("***\n", Color.BLACK);
            outputWindow.addTextToWindow(hunter.toString() + "\n", Color.black);
            outputWindow.addTextToWindow(currentTown.toString() + "\n", Color.black);
            outputWindow.addTextToWindow("(B)uy something at the shop.\n", Color.black);
            outputWindow.addTextToWindow("(S)ell something at the shop.\n", Color.black);
            outputWindow.addTextToWindow("(M)ove on to a different town.\n", Color.black);
            outputWindow.addTextToWindow("(L)ook for trouble!\n", Color.black);
            outputWindow.addTextToWindow("(D)ig for gold!\n", Color.black);
            outputWindow.addTextToWindow("(H)unt for treasure!\n", Color.black);
            outputWindow.addTextToWindow("Give up the hunt and e(X)it.\n", Color.black);
            outputWindow.addTextToWindow("\nWhat's your next move? ", Color.black);
            choice = SCANNER.nextLine().toLowerCase();
            processChoice(choice);
        }
    }

    private void processChoice(String choice) {
        if (choice.equals("b") || choice.equals("s")) {
            currentTown.enterShop(choice);
        } else if (choice.equals("m")) {
            if (currentTown.leaveTown(easyMode)) {
                outputWindow.addTextToWindow(currentTown.getLatestNews() + "\n", Color.BLUE);
                enterTown();
            }
        } else if (choice.equals("l")) {
            currentTown.lookForTrouble();
        } else if (choice.equals("d")) {
            if (!dugOnce) {
                boolean success = currentTown.digForGold(false);
                if (success) {
                    dugOnce = true;
                }
            } else {
                currentTown.digForGold(true);
            }
        } else if (choice.equals("h")) {
            currentTown.huntForTreasure();
        } else if (choice.equals("x")) {
            outputWindow.addTextToWindow("Fare thee well, " + hunter.getHunterName() + "!\n", Color.BLACK);
        } else {
            outputWindow.addTextToWindow("Yikes! That's an invalid option! Try again.\n", Color.RED);
        }
    }
}
