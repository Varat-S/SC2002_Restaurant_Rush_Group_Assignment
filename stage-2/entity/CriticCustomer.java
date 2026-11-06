package entity;

public class CriticCustomer extends Customer {
    private static final int LOSS_PER_WAITING_TURN = 12;

    public CriticCustomer(int customerId) {
        super(customerId);
    }

    public MenuItem chooseMenuItem(Menu menu) {
        MenuItem mostExpensive = menu.getItemAt(0);
        for (int i = 1; i < menu.size(); i++) {
            MenuItem item = menu.getItemAt(i);
            if (item.getPriceCents() > mostExpensive.getPriceCents()) {
                mostExpensive = item;
            }
        }
        return mostExpensive;
    }

    protected int getSatisfactionLossPerTurn() {
        return LOSS_PER_WAITING_TURN;
    }

    protected double getDiscountRate() {
        return 1.0;
    }
}
