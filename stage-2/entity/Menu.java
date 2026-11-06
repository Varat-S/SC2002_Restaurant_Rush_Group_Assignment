package entity;

public class Menu {
    private final MenuItem[] items;

    public Menu() {
        items = MenuItem.values();
    }

    public MenuItem getItemAt(int index) {
        return items[index];
    }

    public int size() {
        return items.length;
    }
}
