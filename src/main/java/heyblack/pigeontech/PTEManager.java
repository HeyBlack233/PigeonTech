package heyblack.pigeontech;

import heyblack.pigeontech.events.PTEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PTEManager {
    private List<PTEGroup> groups;
    private List<String> activePTE = new ArrayList<>(); // should be updated each time PTEManager ticks
    private static final Random random = new Random(((MinecraftServer)FabricLoader.getInstance().getGameInstance()).getWorld(World.OVERWORLD).getSeed());

    private static final PTEManager INSTANCE = new PTEManager();

    public static PTEManager getInstance() {
        return INSTANCE;
    }

    public static boolean isActive(PTEvents pte, World world, ChunkPos chunkPos) {
        for (PTEGroup group : INSTANCE.groups) {
            if (group.getActivePTEAsString().contains(pte.getId())) {
                if (pte.getEffectRange() == PTEvents.EffectRange.DIMENSIONAL) {

                    return group.getWorld() == world.getRegistryKey();

                } else if (pte.getEffectRange() == PTEvents.EffectRange.REGIONAL) {
                    Random r = new Random(group.getSeed() ^ chunkPos.toLong());
                    double portion = group.getPower() / 100 * 0.3;

                    return r.nextDouble() < portion;

                } else {
                    return true;
                }
            }
        }

        return false;
    }

    public void initialize() {
        if (SaveManager.saveExists()) {
            this.groups = SaveManager.loadPTE();

            for (PTEGroup group : this.groups) {
                this.activePTE.addAll(group.getActivePTEAsString());
            }
        }
    }

    public void tick() {
        this.activePTE.clear();

        for (PTEGroup group : groups) {
            group.tick(random);
            this.activePTE.addAll(group.getActivePTEAsString());
        }
    }

//    public PTEvents getPTE() {
//        return this.pte;
//    }
//
//    public static Field getEventWithId(String id) {
//        try {
//            return PTEvents.class.getDeclaredField(id);
//        } catch (NoSuchFieldException e) {
//            PigeonTech.LOGGER.warn("Tried to get event with invalid id");
//
//            return null;
//        } catch (SecurityException s) {
//            return null;
//        }
//    }
//
//    public int viewEventDetail(String id, PlayerEntity player) {
//        Field event = getEventWithId(id);
//
//        if (event != null) {
//            String eventName = event.getAnnotation(Pigeon.class).displayName();
//            String eventDesc = event.getAnnotation(Pigeon.class).desc();
//
//            player.sendMessage(
//                    new LiteralText(eventName).setStyle(
//                            Style.EMPTY.withBold(true).withColor(Formatting.WHITE)
//                    ),
//                    false
//            );
//
//            player.sendMessage(
//                    new LiteralText(eventDesc).setStyle(
//                            Style.EMPTY.withColor(Formatting.GRAY)
//                    ),
//                    false
//            );
//
//            player.sendMessage(new LiteralText(""), false);
//
//            player.sendMessage(
//                    new LiteralText("id: " + id).setStyle(
//                            Style.EMPTY.withColor(Formatting.GRAY)
//                    ),
//                    false
//            );
//
//            try {
//                if (event.getBoolean(pte)) {
//                    player.sendMessage(
//                            new LiteralText("Enabled").setStyle(
//                                    Style.EMPTY.withColor(Formatting.GREEN).withBold(true)
//                            ),
//                            false
//                    );
//                } else {
//                    player.sendMessage(
//                            new LiteralText("Disabled").setStyle(
//                                    Style.EMPTY.withColor(Formatting.RED).withBold(true)
//                            ),
//                            false
//                    );
//                }
//            } catch (IllegalAccessException e) {
//
//            }
//
//            return 1;
//        }
//
//        player.sendMessage(
//                new LiteralText("Specified event does not exist!"),
//                false
//        );
//
//        return 0;
//    }
//
//    public int setEventCommand(PlayerEntity player, String id, boolean bl) {
//        Field event = getEventWithId(id);
//
//        if (event != null) {
//            try {
//                boolean oldValue = event.getBoolean(pte);
//
//                if (oldValue == bl) {
//                    player.sendMessage(
//                            new LiteralText(
//                                    "The event is already " + (oldValue ? "activated!" : "disabled!")
//                            ).setStyle(
//                                    Style.EMPTY.withColor(Formatting.GRAY)
//                            ),
//                            false
//                    );
//                } else {
//                    event.setBoolean(pte, bl);
//
//                    player.sendMessage(
//                            new LiteralText(
//                                    "Event (" + id + ") has been " + (bl ? "activated!" : "disabled!")
//                            ).setStyle(
//                                    Style.EMPTY.withColor(Formatting.GRAY)
//                            ),
//                            false
//                    );
//                }
//
//                return 1;
//            } catch (IllegalAccessException e) {
//                return 0;
//            }
//
//        }
//
//        player.sendMessage(
//                new LiteralText("Specified event does not exist!"),
//                false
//        );
//
//        return 0;
//    }
//
//    public int showActiveEvents(PlayerEntity player) {
//        List<Field> list = getActiveEvents();
//
//        if (!list.isEmpty()) {
//            player.sendMessage(
//                    new LiteralText("Active events:").setStyle(
//                            Style.EMPTY.withBold(true)
//                    ),
//                    false
//            );
//
//            for (Field field : list) {
//                player.sendMessage(
//                        new LiteralText(
//                                field.getAnnotation(Pigeon.class).displayName() + " (" + field.getName() + ")"
//                        ).setStyle(
//                                Style.EMPTY.withColor(Formatting.GRAY)
//                        ),
//                        false
//                );
//            }
//
//            return 1;
//        }
//
//        player.sendMessage(
//                new LiteralText("There's no event active currently").setStyle(
//                        Style.EMPTY.withColor(Formatting.GRAY)
//                ),
//                false
//        );
//
//        return 0;
//    }
//
//    public List<Field> getActiveEvents() {
//        List<Field> list = new ArrayList<>();
//
//        for (Field field : PTEvents.class.getDeclaredFields()) {
//            try {
//                if (field.getBoolean(pte)) {
//                    list.add(field);
//                }
//
//            } catch (IllegalAccessException e) {
//
//            }
//        }
//
//        return list;
//    }
//
//    public void setEvent(String id, boolean bl) {
//        try {
//            this.pte.getClass().getDeclaredField(id).setBoolean(pte, bl);
//            onEventTrigger(id);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            PigeonTech.LOGGER.warn("Error when setting pte");
//        }
//    }
//
//    public void onEventTrigger(String id) {
//
//    }
//
}
