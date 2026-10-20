package entity;

public class Waiter extends Staff {
    public Waiter() {
        super();
    }

    public boolean seatCustomer(Customer c, Table t) {
        if (hasActed() || c == null || t == null) {
            return false;
        }
        if (c.getState() != CustomerState.WAITING) {
            return false;
        }
        if (!t.seat(c)) {
            return false;
        }
        actedThisTurn = true;
        return true;
    }

    public Order takeOrder(Customer c, MenuItem item) {
        if (hasActed() || c == null || item == null) {
            return null;
        }
        if (c.getState() != CustomerState.SEATED || c.getOrder() != null) {
            return null;
        }
        actedThisTurn = true;
        return new Order(c.getCustomerId(), item, c);
    }

    public boolean serveOrder(Order o) {
        if (hasActed() || o == null || o.getStatus() != OrderStatus.READY) {
            return false;
        }
        o.markServed();
        actedThisTurn = true;
        return true;
    }
}
