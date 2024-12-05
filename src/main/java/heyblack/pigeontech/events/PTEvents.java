package heyblack.pigeontech.events;

import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public abstract class PTEvents {
   private String id;
   private String displayName;
   private EffectRange effectRange;
   private EventDuration duration;

   @Nullable
   private World activeWorld;
   @Nullable
   private Random effectRegion;
   private double effectRegionPortion;

   public PTEvents(String id, String displayName, EffectRange effectRange, EventDuration duration) {
      this.id = id;
      this.displayName = displayName;
      this.effectRange = effectRange;
      this.duration = duration;
   }

   public abstract void onTrigger();

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
