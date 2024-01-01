import java.awt.Color;
import java.util.Scanner;
public class TreasureHunterRunner {
    public static void main(String[] args) {
        OutputWindow window = new OutputWindow();
        TreasureHunter game = new TreasureHunter(window);
        game.play();
    }
}