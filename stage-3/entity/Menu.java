package entity;

public class Menu {
    private final MenuItem[] items;

    public Menu() {
        items = new MenuItem[] {
            StandardMenuItem.SALAD,
            StandardMenuItem.BURGER,
            StandardMenuItem.PASTA,
            new ComboMeal(StandardMenuItem.SALAD, StandardMenuItem.PASTA)
        };
    }

    public MenuItem getItemAt(int index) {
        return items[index];
    }

    public int size() {
        return items.length;
    }
}
