package heyblack.pigeontech.events;

import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class PTEvent {
   private final String id;
   private final String displayName;
   private final EffectRange effectRange;
   private final EventDuration duration;

   @Nullable
   private World activeWorld;
   @Nullable
   private Random effectRegion;
   private double effectRegionPortion;

   public PTEvent(String id, String displayName, EffectRange effectRange, EventDuration duration) {
      // properties of each event should be assigned in their respective constructor
      this.id = id;
      this.displayName = displayName;
      this.effectRange = effectRange;
      this.duration = duration;
   }

   public abstract void trigger();

   public String getId() {
      return this.id;
   }

   public String getDisplayName(){
      return this.displayName;
   }

   public EffectRange getEffectRange() {
      return this.effectRange;
   }

   public EventDuration getDuration() {
      return this.duration;
   }

   public enum EffectRange {
      GLOBAL,
      DIMENSIONAL,
      REGIONAL
   }

   public enum EventDuration {
      ON_TRIGGER,
      PERSISTENT
   }
}
