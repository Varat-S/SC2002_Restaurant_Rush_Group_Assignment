package entity;

public class RegularCustomer extends Customer {
    private static final int LOSS_PER_WAITING_TURN = 8;

    public RegularCustomer(int customerId) {
        super(customerId);
    }

    protected int getSatisfactionLossPerTurn() {
        return LOSS_PER_WAITING_TURN;
    }

    protected double getDiscountRate() {
        return 1.0;
    }
}
