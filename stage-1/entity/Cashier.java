package entity;

public class Cashier extends Staff {
    public Cashier() {
        super();
    }

    public int collectPayment(Customer c) {
        if (hasActed() || c == null || c.getState() != CustomerState.READY_TO_PAY) {
            return -1;
        }
        if (c.getOrder() == null) {
            return -1;
        }
        int amount = c.getOrder().getPriceCents();
        if (!c.markPaid()) {
            return -1;
        }
        actedThisTurn = true;
        return amount;
    }
}
