package heyblack.pigeontech.events;

public abstract class PersistentEvent extends PTEvent {
    private int lifetime;

    public PersistentEvent(String id, String displayName, EffectRange effectRange, EventDuration duration, int lifetime) {
        super(id, displayName, effectRange, duration);
        this.lifetime = lifetime;
    }

    public abstract void tick();

    public abstract void terminate();
}
