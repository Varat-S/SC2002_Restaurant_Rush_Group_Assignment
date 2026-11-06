package entity;

public class Chef extends Staff {
    public Chef() {
        super();
    }

    public boolean advanceOrder(Order o) {
        if (hasActed() || o == null || o.getStatus() != OrderStatus.PLACED) {
            return false;
        }
        o.advancePreparation();
        actedThisTurn = true;
        return true;
    }
}
