package entity;

public class Table {
    private final int tableId;
    private Customer seatedCustomer;

    public Table(int tableId) {
        this.tableId = tableId;
    }

    public boolean seat(Customer c) {
        if (seatedCustomer != null || c == null || c.getTable() != null
                || !c.markSeated()) {
            return false;
        }
        seatedCustomer = c;
        c.assignTable(this);
        return true;
    }

    public void free() {
        if (seatedCustomer != null) {
            seatedCustomer.assignTable(null);
            seatedCustomer = null;
        }
    }

    public boolean isOccupied() {
        return seatedCustomer != null;
    }

    public Customer getSeatedCustomer() {
        return seatedCustomer;
    }

    public int getTableId() {
        return tableId;
    }
}
