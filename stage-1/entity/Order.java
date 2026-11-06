package entity;

public class Order {
    private final int orderId;
    private final MenuItem item;
    private final int priceCents;
    private int remainingPrepUnits;
    private OrderStatus status;

    public Order(int orderId, MenuItem item, Customer customer) {
        this.orderId = orderId;
        this.item = item;
        this.priceCents = item.getPriceCents();
        this.remainingPrepUnits = item.getPrepUnits();
        this.status = OrderStatus.PLACED;
        customer.assignOrder(this);
    }

    public boolean advancePreparation() {
        if (status != OrderStatus.PLACED) {
            return false;
        }
        if (remainingPrepUnits > 0) {
            remainingPrepUnits--;
        }
        if (remainingPrepUnits == 0) {
            status = OrderStatus.READY;
        }
        return status == OrderStatus.READY;
    }

    public boolean isReady() {
        return status == OrderStatus.READY;
    }

    public void markServed() {
        if (status == OrderStatus.READY) {
            status = OrderStatus.SERVED;
        }
    }

    public boolean markPaid() {
        if (status != OrderStatus.SERVED) {
            return false;
        }
        status = OrderStatus.PAID;
        return true;
    }

    public void cancel() {
        status = OrderStatus.CANCELLED;
    }

    public int getOrderId() {
        return orderId;
    }

    public MenuItem getItem() {
        return item;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public int getRemainingPrepUnits() {
        return remainingPrepUnits;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
