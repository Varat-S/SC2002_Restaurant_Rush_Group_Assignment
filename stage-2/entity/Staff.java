package entity;

public abstract class Staff {
    protected boolean actedThisTurn;

    public Staff() {
        actedThisTurn = false;
    }

    public boolean hasActed() {
        return actedThisTurn;
    }

    public void resetForTurn() {
        actedThisTurn = false;
    }
}
