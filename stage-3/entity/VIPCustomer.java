package entity;

public class VIPCustomer extends Customer {
    private static final int LOSS_PER_WAITING_TURN = 5;
    private static final double DISCOUNT_RATE = 0.9;

    public VIPCustomer(int customerId) {
        super(customerId);
    }

    protected int getSatisfactionLossPerTurn() {
        return LOSS_PER_WAITING_TURN;
    }

    protected double getDiscountRate() {
        return DISCOUNT_RATE;
    }
}
