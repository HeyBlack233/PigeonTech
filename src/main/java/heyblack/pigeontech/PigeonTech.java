package heyblack.pigeontech;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

import static net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTED;
import static net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPING;
//import static net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.START_SERVER_TICK;
//import static net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.START_WORLD_TICK;

public class PigeonTech implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger();
    private static final FabricLoader LOADER = FabricLoader.getInstance();
    public static EventManager eventManager = new EventManager();


    @Override
    public void onInitialize() {
        if (LOADER.getEnvironmentType() == EnvType.SERVER) {
            SERVER_STARTED.register(
                    server -> {
                        eventManager.initialize(ConfigManager.initializePTE());
                    }
            );

            SERVER_STOPPING.register(
                    server -> {
                        ConfigManager.savePTE(eventManager.getPTE());
                    }
            );

            //        START_WORLD_TICK.register(
//                world -> {
//
//                }
//        );
//
//        START_SERVER_TICK.register(
//                server -> {
//
//                }
//        );

            CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> dispatcher.register(
                    CommandManager.literal("pigeontech")
                            .then(CommandManager.literal("event")
                                    .then(CommandManager.argument("event", StringArgumentType.string())
                                            .suggests(
                                                    (ctx, builder) -> {
                                                        for (Field field : PTEvents.class.getDeclaredFields()) {
                                                            builder.suggest(field.getName());
                                                        }

                                                        return builder.buildFuture();
                                                    }
                                            )
                                            .executes(ctx -> eventManager.viewEventDetail(
                                                    StringArgumentType.getString(ctx, "event"),
                                                    ctx.getSource().getPlayer()
                                            ))
                                            .then(CommandManager.argument("enable", BoolArgumentType.bool())
                                                    .executes(ctx -> eventManager.setEventCommand(
                                                            ctx.getSource().getPlayer(),
                                                            StringArgumentType.getString(ctx, "event"),
                                                            BoolArgumentType.getBool(ctx, "enable")
                                                    ))
                                            )
                                    )
                            )
                            .then(CommandManager.literal("listactivated")
                                    .executes(ctx -> eventManager.showActiveEvents(ctx.getSource().getPlayer()))
                            )
            )));
        }
    }
}
