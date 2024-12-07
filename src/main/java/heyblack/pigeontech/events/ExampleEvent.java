package heyblack.pigeontech.events;

public class ExampleEvent extends PTEvent {
    public ExampleEvent(String id, String displayName, EffectRange effectRange, EventDuration duration) {
        super(id, displayName, effectRange, duration);
    }

    @Override
    public void trigger() {

    }
}
