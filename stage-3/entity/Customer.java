package entity;

public abstract class Customer {
    protected final int customerId;
    private int satisfaction;
    private int satisfactionWhenPaid;
    private Order order;
    private Table table;
    private CustomerState state;

    public Customer(int customerId) {
        this.customerId = customerId;
        this.satisfaction = 100;
        this.state = CustomerState.WAITING;
    }

    public MenuItem chooseMenuItem(Menu menu) {
        // Regular and VIP customers use ID modulo menu size; index 3 is the combo.
        return menu.getItemAt(customerId % menu.size());
    }

    public void loseSatisfaction() {
        satisfaction = Math.max(0, satisfaction - getSatisfactionLossPerTurn());
    }

    public void recoverSatisfaction(int points) {
        if (isAwaitingService()) {
            satisfaction = Math.min(100, satisfaction + points);
        }
    }

    public int applyBillDiscount(int cents) {
        return (int) Math.round(cents * getDiscountRate());
    }

    protected abstract int getSatisfactionLossPerTurn();

    protected abstract double getDiscountRate();

    public boolean markPaid() {
        if (state != CustomerState.READY_TO_PAY || order == null || !order.markPaid()) {
            return false;
        }
        satisfactionWhenPaid = satisfaction;
        state = CustomerState.PAID;
        return true;
    }

    public boolean isAwaitingService() {
        if (state == CustomerState.WAITING) {
            return true;
        }
        if (state != CustomerState.SEATED) {
            return false;
        }
        return order == null
                || order.getStatus() == OrderStatus.PLACED
                || order.getStatus() == OrderStatus.READY;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public int getSatisfactionWhenPaid() {
        return satisfactionWhenPaid;
    }

    public Order getOrder() {
        return order;
    }

    public Table getTable() {
        return table;
    }

    boolean markSeated() {
        if (state != CustomerState.WAITING) {
            return false;
        }
        state = CustomerState.SEATED;
        return true;
    }

    public boolean startEating() {
        if (state != CustomerState.SEATED || order == null
                || order.getStatus() != OrderStatus.SERVED) {
            return false;
        }
        state = CustomerState.EATING;
        return true;
    }

    public boolean finishEating() {
        if (state != CustomerState.EATING) {
            return false;
        }
        state = CustomerState.READY_TO_PAY;
        return true;
    }

    public boolean leave() {
        if (satisfaction != 0
                || (state != CustomerState.WAITING && state != CustomerState.SEATED)) {
            return false;
        }
        state = CustomerState.LEFT;
        return true;
    }

    public CustomerState getState() {
        return state;
    }

    void assignOrder(Order order) {
        this.order = order;
    }

    void assignTable(Table table) {
        this.table = table;
    }
}
