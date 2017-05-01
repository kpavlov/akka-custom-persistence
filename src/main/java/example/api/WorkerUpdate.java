package example.api;

public class WorkerUpdate {
    
    private final int health;
    private final int mana;

    public WorkerUpdate(int health, int mana) {
        this.health = health;
        this.mana = mana;
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WorkerUpdate{");
        sb.append("health=").append(health);
        sb.append(", mana=").append(mana);
        sb.append('}');
        return sb.toString();
    }
}
