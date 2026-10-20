import boundary.GameUI;
import control.GameController;
import entity.Restaurant;

public class Main {
    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant();
        GameUI ui = new GameUI();
        GameController controller = new GameController(restaurant, ui);
        controller.playGame();
    }
}
