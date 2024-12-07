package heyblack.pigeontech;

import heyblack.pigeontech.events.PTEvent;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PTEGroup {
    private final int power; // max: 100
    private final RegistryKey<World> world;
    private List<PTEvent> activePTE;
    private Long seed;

    PTEGroup(int power, World world) {
        this.power = power;
        this.world = world.getRegistryKey();
        this.activePTE = this.populate();
    }

    public List<PTEvent> populate() {
        // an event should only be active in one group
        // should check whether an event is already active before adding it to the group

        List<PTEvent> list = new ArrayList<>();

        // TODO: implement code to populate the PTEGroup

        return list;
    }

    public void tick(Random random) {

        // this should be the last part of tick method
        for (PTEvent pte : this.activePTE) {
            if (pte.getEffectRange() == PTEvent.EffectRange.REGIONAL) {
                return;
            }
        }

        this.seed = random.nextLong();
    }

    public List<String> getActivePTEAsString() {
        List<String> list = new ArrayList<>();
        for (PTEvent pte : this.activePTE) {
            list.add(pte.getId());
        }

        return list;
    }

    public RegistryKey<World> getWorld() {
        return this.world;
    }

    public float getPower() {
        return this.power;
    }

    public Long getSeed() {
        return this.seed;
    }
}
