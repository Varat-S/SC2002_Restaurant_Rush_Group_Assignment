package entity;

public class Host extends Staff {
    public Host() {
        super();
    }

    public boolean seatCustomer(Customer c, Table t) {
        if (hasActed() || c == null || t == null) {
            return false;
        }
        if (c.getState() != CustomerState.WAITING || !t.seat(c)) {
            return false;
        }
        actedThisTurn = true;
        return true;
    }
}
