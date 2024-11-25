package heyblack.pigeontech;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PTEManager {
    private static PTEManager instance = new PTEManager();

    private PTEvents pte;

    PTEManager() {

    }

    public void initialize(PTEvents pte) {
        this.pte = pte;
    }

    public PTEvents getPTE() {
        return this.pte;
    }

    public void onEventTrigger() {

    }

    public static Field getEventWithId(String id) {
        try {
            return PTEvents.class.getDeclaredField(id);
        } catch (NoSuchFieldException e) {
            PigeonTech.LOGGER.warn("Tried to get event with invalid id");

            return null;
        } catch (SecurityException s) {
            return null;
        }
    }

    public int viewEventDetail(String id, PlayerEntity player) {
        Field event = getEventWithId(id);

        if (event != null) {
            String eventName = event.getAnnotation(Pigeon.class).displayName();
            String eventDesc = event.getAnnotation(Pigeon.class).desc();

            player.sendMessage(
                    new LiteralText(eventName).setStyle(
                            Style.EMPTY.withBold(true).withColor(Formatting.WHITE)
                    ),
                    false
            );

            player.sendMessage(
                    new LiteralText(eventDesc).setStyle(
                            Style.EMPTY.withColor(Formatting.GRAY)
                    ),
                    false
            );

            player.sendMessage(new LiteralText(""), false);

            player.sendMessage(
                    new LiteralText("id: " + id).setStyle(
                            Style.EMPTY.withColor(Formatting.GRAY)
                    ),
                    false
            );

            try {
                if (event.getBoolean(pte)) {
                    player.sendMessage(
                            new LiteralText("Enabled").setStyle(
                                    Style.EMPTY.withColor(Formatting.GREEN).withBold(true)
                            ),
                            false
                    );
                } else {
                    player.sendMessage(
                            new LiteralText("Disabled").setStyle(
                                    Style.EMPTY.withColor(Formatting.RED).withBold(true)
                            ),
                            false
                    );
                }
            } catch (IllegalAccessException e) {

            }

            return 1;
        }

        player.sendMessage(
                new LiteralText("Specified event does not exist!"),
                false
        );

        return 0;
    }

    public int setEventCommand(PlayerEntity player, String id, boolean bl) {
        Field event = getEventWithId(id);

        if (event != null) {
            try {
                boolean oldValue = event.getBoolean(pte);

                if (oldValue == bl) {
                    player.sendMessage(
                            new LiteralText(
                                    "The event is already " + (oldValue ? "activated!" : "disabled!")
                            ).setStyle(
                                    Style.EMPTY.withColor(Formatting.GRAY)
                            ),
                            false
                    );
                } else {
                    event.setBoolean(pte, bl);

                    player.sendMessage(
                            new LiteralText(
                                    "Event (" + id + ") has been " + (bl ? "activated!" : "disabled!")
                            ).setStyle(
                                    Style.EMPTY.withColor(Formatting.GRAY)
                            ),
                            false
                    );
                }

                return 1;
            } catch (IllegalAccessException e) {
                return 0;
            }

        }

        player.sendMessage(
                new LiteralText("Specified event does not exist!"),
                false
        );

        return 0;
    }

    public int showActiveEvents(PlayerEntity player) {
        List<Field> list = getActiveEvents();

        if (!list.isEmpty()) {
            player.sendMessage(
                    new LiteralText("Active events:").setStyle(
                            Style.EMPTY.withBold(true)
                    ),
                    false
            );

            for (Field field : list) {
                player.sendMessage(
                        new LiteralText(
                                field.getAnnotation(Pigeon.class).displayName() + " (" + field.getName() + ")"
                        ).setStyle(
                                Style.EMPTY.withColor(Formatting.GRAY)
                        ),
                        false
                );
            }

            return 1;
        }

        player.sendMessage(
                new LiteralText("There's no event active currently").setStyle(
                        Style.EMPTY.withColor(Formatting.GRAY)
                ),
                false
        );

        return 0;
    }

    public List<Field> getActiveEvents() {
        List<Field> list = new ArrayList<>();

        for (Field field : PTEvents.class.getDeclaredFields()) {
            try {
                if (field.getBoolean(pte)) {
                    list.add(field);
                }

            } catch (IllegalAccessException e) {

            }
        }

        return list;
    }

    public void setEvent(String id, boolean bl) {
        Field event = getEventWithId(id);
    }
}
