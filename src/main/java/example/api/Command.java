package example.api;

public abstract class Command extends AbstractMessage {
    public Command(String id) {
        super(id);
    }
}
