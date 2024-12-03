package heyblack.pigeontech;

import net.minecraft.world.World;

import java.util.List;

public class PTEGroup {
    private final float power;
    private final World world;
    private List<String> list;

    private final PTEvents pte = new PTEvents();

    PTEGroup(float power, World world) {
        this.power = power;
        this.world = world;
    }

}
