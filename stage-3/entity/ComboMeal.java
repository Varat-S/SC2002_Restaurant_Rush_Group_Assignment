package entity;

public class ComboMeal implements MenuItem {
    private final MenuItem first;
    private final MenuItem second;
    private final String name;
    private final int priceCents;
    private final int prepUnits;

    public ComboMeal(MenuItem first, MenuItem second) {
        this.first = first;
        this.second = second;
        name = this.first.getName() + " + " + this.second.getName() + " Combo";
        priceCents = (int) Math.round((this.first.getPriceCents()
                + this.second.getPriceCents()) * 0.9);
        prepUnits = this.first.getPrepUnits() + this.second.getPrepUnits();
    }

    public String getName() {
        return name;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public int getPrepUnits() {
        return prepUnits;
    }
}
