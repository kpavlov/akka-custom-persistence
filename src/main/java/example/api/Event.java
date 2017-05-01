package example.api;


public class Event extends AbstractMessage {

    private final WorkerUpdate update;

    public Event(String id, WorkerUpdate update) {
        super(id);
        this.update = update;
    }

    public WorkerUpdate getUpdate() {
        return update;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Event{");
        sb.append("id=").append(getId());
        sb.append(", update=").append(update);
        sb.append('}');
        return sb.toString();
    }
}
